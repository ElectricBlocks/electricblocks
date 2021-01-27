package edu.uidaho.electricblocks.guis;

import org.lwjgl.glfw.GLFW;

import edu.uidaho.electricblocks.electric.Volt;
import edu.uidaho.electricblocks.tileentities.ExternalGridTileEntity;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class ExternalGridScreen extends Screen {
    // Layout constants 
    // Width of a button
    private static final int BUTTON_WIDTH = 200;
    // Height of a button
    private static final int BUTTON_HEIGHT = 20;
    // Width of text input
    private static final int TEXT_INPUT_WIDTH = 100;
    // Height of text input
    private static final int TEXT_INPUT_HEIGHT = 20;
    // Distance from bottom of the screen to the "Done" button's top
    private static final int DONE_BUTTON_TOP_OFFSET = 26;
    // Distance from top of the screen to this GUI's title
    private static final int TITLE_HEIGHT = 8;

    // Layout elements
    private Button doneButton;
    private TextFieldWidget textFieldVoltage;
    private TextFieldWidget textFieldResultPower;
    private TextFieldWidget textFieldReactivePower;

    // Info needed to preload the form with data
    private boolean changed = false;
    private ExternalGridTileEntity externalGridTileEntity;
    private PlayerEntity player;
    private boolean inService;

    public ExternalGridScreen(ExternalGridTileEntity externalGridTileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.externalgridscreen"));
        this.externalGridTileEntity = externalGridTileEntity;
        this.player = player;
    }

    @Override
    protected void init() {
        inService = externalGridTileEntity.isInService();
        textFieldVoltage = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldVoltage.setText(String.format("%f", externalGridTileEntity.getVoltage().getVolts()));
        textFieldVoltage.setFocused2(true);
        textFieldVoltage.setVisible(true);

        textFieldResultPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 60, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldResultPower.setText(String.format("%f", externalGridTileEntity.getResultPower().getMegaWatts()));
        textFieldResultPower.setVisible(true);

        textFieldReactivePower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePower.setText(String.format("%f", externalGridTileEntity.getReactivePower().getMegaWatts()));
        textFieldReactivePower.setVisible(true); 

        // Add the "Done" button
        doneButton = new Button(
            (this.width - BUTTON_WIDTH) / 2,
            this.height - DONE_BUTTON_TOP_OFFSET,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            // Text shown on the button
            I18n.format("gui.done"),
            // Action performed when the button is pressed
            button -> onClose()
        );
        this.addButton(doneButton);

        this.addButton(new Button(
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
            }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        // First draw the background of the screen
        this.renderBackground();
        // Draw the title
        this.drawCenteredString(this.font, this.title.getFormattedText(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        // Draw property labels
        this.drawString(this.font, "Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "V", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MVar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw separator
        this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, 45 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        textFieldVoltage.render(mouseX, mouseY, partialTicks);
        textFieldResultPower.render(mouseX, mouseY, partialTicks);
        textFieldReactivePower.render(mouseX, mouseY, partialTicks);
        // Call the super class' method to complete rendering
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        if (textFieldVoltage.isFocused()) {
            textFieldVoltage.charTyped(p_charTyped_1_, p_charTyped_2_);
            onChange();
        }
        return super.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (textFieldVoltage.isFocused()) {
            textFieldVoltage.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
            if (p_keyPressed_1_ == GLFW.GLFW_KEY_BACKSPACE) {
                onChange();
            }
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (textFieldVoltage.isFocused()) {
            textFieldVoltage.keyReleased(keyCode, scanCode, modifiers);
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void onClose() {
        if (changed) {
            boolean shouldUpdate = true;
            double voltage = 0;
            try {
                voltage = Double.parseDouble(textFieldVoltage.getText());
            } catch (NumberFormatException e) {
                shouldUpdate = false;
                PlayerUtils.error(player, "gui.electricblocks.err_invalid_number");
            }

            if (shouldUpdate) {
                PlayerUtils.sendMessage(player, "command.electricblocks.viewmodify.submit");
                externalGridTileEntity.setInService(inService);
                externalGridTileEntity.setVoltage(new Volt(voltage));
                externalGridTileEntity.notifyUpdate();

                externalGridTileEntity.requestSimulation(player);
            }
        }
        super.onClose();
    }

    public void onChange() {
        if (!changed) {
            changed = true;
            doneButton.setMessage("* " + I18n.format("gui.done") + " *");
        }
    }
}
