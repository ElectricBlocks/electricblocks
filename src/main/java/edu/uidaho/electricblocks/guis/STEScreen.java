package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.network.ElectricBlocksPacketHandler;
import edu.uidaho.electricblocks.network.TileEntityMessageToServer;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The STEScreen class is designed to automatically construct the view/modify GUI based on the information stored within
 * a simulation tile entity. This single class is aware of which tile entity that is trying to be displayed and will use
 * their properties accordingly.
 *
 * Previously every single STE had its own Screen subclass but this resulted in far too much duplicate code.
 */
public class STEScreen extends Screen {
    // Layout constants 
    // Width of a button
    protected static final int BUTTON_WIDTH = 200;
    // Height of a button
    protected static final int BUTTON_HEIGHT = 20;
    // Width of text input
    protected static final int TEXT_INPUT_WIDTH = 100;
    // Height of text input
    protected static final int TEXT_INPUT_HEIGHT = 20;
    // Distance from bottom of the screen to the "Done" button's top
    protected static final int DONE_BUTTON_TOP_OFFSET = 26;
    // Distance from top of the screen to this GUI's title
    protected static final int TITLE_HEIGHT = 8;
    protected static final int ENABLED_COLOR = 14737632;

    // Constants
    private static final int MAX_SCROLL = 0;
    private static final int SCROLL_CUTOFF_MIN = 20;

    // Layout elements
    private int scrollPosition = 0;
    int offset = 25;
    int separatorOffset = 0;
    private int MIN_SCROLL = -420;
    private final List<PropertyRow> propertyRows = new ArrayList<>();
    private boolean donePressed = false;
    protected Button doneButton;
    protected boolean inService;
    protected Button inServiceButton;

    // Info needed to preload the form with data
    protected boolean changed = false;
    protected final PlayerEntity player;
    protected final SimulationTileEntity simulationTileEntity;

    public STEScreen(SimulationTileEntity simulationTileEntity, PlayerEntity player) {
        super(new TranslationTextComponent(simulationTileEntity.getTranslationString()));
        this.simulationTileEntity = simulationTileEntity;
        this.player = player;
        this.inService = simulationTileEntity.isInService();
    }

    /**
     * Called whenever a new screen is initialized. This function adds a "Done" button and an "In Service" button to the
     * bottom of the screen as well as extracting the properties from the STE to create all the necessary inputs and
     * labels.
     */
    @Override
    protected void init() {
        // Add in service button
        inServiceButton = new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - 2 * DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                inService ? "In Service: true" : "In Service: false",
                button -> {
                    if (inService) {
                        button.setMessage("In Service: false");
                        inService = false;
                    } else {
                        button.setMessage("In Service: true");
                        inService = true;
                    }
                    onChange();
                });
        this.addButton(inServiceButton);
        // Add the "Done" button
        doneButton = new Button(
            (this.width - BUTTON_WIDTH) / 2,
            this.height - DONE_BUTTON_TOP_OFFSET,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            // Text shown on the button
            I18n.format("gui.electricblocks.close"),
            // Action performed when the button is pressed
            button -> {
                donePressed = true;
                onClose();
            }
        );
        this.addButton(doneButton);

        // Process all of the inputs
        boolean isFirst = true;
        for (Map.Entry<String, SimulationProperty> entry : simulationTileEntity.getInputs().entrySet()) {
            if (entry.getValue().getPropertyType() != SimulationProperty.PropertyType.DOUBLE) {
                // Skip over special properties that are not doubles
                continue;
            }
            // Initialize a new property row with info from inputs
            PropertyRow propertyRow = new PropertyRow();
            propertyRow.isInput = true;
            propertyRow.offset = offset;
            propertyRow.simulationProperty = entry.getValue();
            propertyRow.textFieldWidget = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, offset, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
            propertyRow.textFieldWidget.setText(String.format("%f", entry.getValue().getDouble()));
            propertyRow.textFieldWidget.setVisible(true);
            addButton(propertyRow.textFieldWidget);
            if (isFirst) {
                isFirst = false;
                propertyRow.textFieldWidget.setFocused2(true);
                setFocused(propertyRow.textFieldWidget);
            }
            offset += 30;
            propertyRows.add(propertyRow);
        }
        // Remember separator location
        separatorOffset = offset - 10;
        offset += 5;
        // Process all of the outputs
        for (Map.Entry<String, SimulationProperty> entry : simulationTileEntity.getOutputs().entrySet()) {
            if (entry.getValue().getPropertyType() != SimulationProperty.PropertyType.DOUBLE) {
                continue;
            }
            PropertyRow propertyRow = new PropertyRow();
            propertyRow.offset = offset;
            propertyRow.simulationProperty = entry.getValue();
            propertyRow.textFieldWidget = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, offset, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
            propertyRow.textFieldWidget.setText(String.format("%f", entry.getValue().getDouble()));
            initializeResultField(propertyRow.textFieldWidget);
            offset += 30;
            propertyRows.add(propertyRow);
        }
        MIN_SCROLL = -(offset - 180);

        // Hide all of the text inputs that shouldn't be visible because they are off screen
        for (Widget w : this.buttons) {
            if (!(w instanceof Button)) {
                w.visible = w.y <= getMaxCutoff() && w.y >= SCROLL_CUTOFF_MIN;
            }
        }
    }

    /**
     * Handles mouse scrolling for hiding and displaying elements that are outside of the scroll window
     * @param mouseX Mouse x position
     * @param mouseY Mouse y position
     * @param scrollAmount Amount scrolled
     * @return Boolean value from super method call
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollAmount) {
        int scroll = scrollAmount >= 0 ? 5 : -5;
        if (scrollPosition + scroll >= MIN_SCROLL && scrollPosition + scroll <= MAX_SCROLL) {
            scrollPosition += scroll;
            for (Widget w : this.buttons) {
                if (!(w instanceof Button)) {
                    w.y += scroll;
                    w.visible = w.y <= getMaxCutoff() && w.y >= SCROLL_CUTOFF_MIN;
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // res will be false for all characters that update the display except for backspace
        boolean res = super.keyPressed(keyCode, scanCode, modifiers); 
        boolean updateKeyPressed = !res || keyCode == 259; // Check if backspace is pressed or other update key is pressed
        boolean focusIsOnEditableTextFieldWidget = this.getFocused() instanceof TextFieldWidget && ((TextFieldWidget) this.getFocused()).canWrite();
        if (updateKeyPressed && focusIsOnEditableTextFieldWidget) {
            onChange();
        }
        return res;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        // First draw the background of the screen
        this.renderBackground();
        // Draw the title
        this.drawCenteredString(this.font, this.title.getFormattedText(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);

        for (PropertyRow pr : propertyRows) {
            this.drawString(this.font, pr.simulationProperty.getLabel(), (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + pr.offset + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
            this.drawString(this.font, pr.simulationProperty.getUnits(), (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + pr.offset + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        }

        this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, scrollPosition + separatorOffset + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawString(FontRenderer p_drawString_1_, String p_drawString_2_, int p_drawString_3_, int p_drawString_4_, int p_drawString_5_) {
        if (p_drawString_4_ <= getMaxCutoff() && p_drawString_4_ >= SCROLL_CUTOFF_MIN)
            super.drawString(p_drawString_1_, p_drawString_2_, p_drawString_3_, p_drawString_4_, p_drawString_5_);
    }

    /**
     * This function is called whenever a change is made by modifying a field value or by toggling the "In Service"
     * button. This lets the form know that a submission needs to be made and updates the "Done" button message
     */
    public void onChange() {
        if (!changed) {
            changed = true;
            doneButton.setMessage("* " + I18n.format("gui.electricblocks.submitchanges") + " *");
        }
    }

    /**
     * Called when this screen is closed. Used to finish processing the form.
     */
    @Override
    public void onClose() {
        if (changed) {
            if (donePressed) {
                submitChanges();
            } else {
                PlayerUtils.warnClient(player, "gui.electricblocks.warn_escape");
            }
        }
        super.onClose();
    }

    /**
     * This function is called when a player pressed the done button after
     * making changes. Subclasses should implement form validation, tile entity
     * updates, and simulation requests in this function.
     */
    protected void submitChanges() {
        boolean shouldUpdate = true;
        // Validate all the values entered into rows are correct
        for (PropertyRow pr : propertyRows) {
            if (!pr.isInput) {
                continue;
            }
            try {
                pr.newValue = Double.parseDouble(pr.textFieldWidget.getText());
            } catch (NumberFormatException e) {
                shouldUpdate = false;
                PlayerUtils.errorClient(player,"gui.electricblocks.err_invalid_number");
            }
        }

        // Construct and send network packet to server
        if (shouldUpdate) {
            PlayerUtils.sendMessageClient(player,"command.electricblocks.viewmodify.submit");
            simulationTileEntity.setInService(inService);
            for (PropertyRow pr : propertyRows) {
                if (!pr.isInput) {
                    continue;
                }
                pr.simulationProperty.set(pr.newValue);
            }
            TileEntityMessageToServer teMSG = new TileEntityMessageToServer(simulationTileEntity, player);
            ElectricBlocksPacketHandler.INSTANCE.sendToServer(teMSG);
        }

    }

    /**
     * Boiler plate code for initializing an output result field
     * @param textFieldWidget The text field widget being initialized
     */
    protected void initializeResultField(TextFieldWidget textFieldWidget) {
        textFieldWidget.setVisible(true);
        textFieldWidget.setEnabled(false);
        textFieldWidget.setDisabledTextColour(ENABLED_COLOR);
        addButton(textFieldWidget);
    }

    /**
     * Calculates the cutoff point at which text fields should no longer be displayed to avoid overlap with buttons.
     * @return The max y cutoff for this screen.
     */
    public int getMaxCutoff() {
        return inServiceButton.y - BUTTON_HEIGHT;
    }

    /**
     * Static inner class used to represent a single row in the GUI. Each row consists of a label, a text field, and a
     * units label. This is used to help group all the relevant data together.
     */
    private static class PropertyRow {
        public boolean isInput = false;
        public int offset = 0;
        public TextFieldWidget textFieldWidget;
        public SimulationProperty simulationProperty;
        public double newValue;
    }
    
}
