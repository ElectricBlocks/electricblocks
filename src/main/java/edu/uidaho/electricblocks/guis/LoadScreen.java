package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.tileentities.LoadTileEntity;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class LoadScreen extends Screen {
    /** Width of a button */
    private static final int BUTTON_WIDTH = 200;
    /** Height of a button */
    private static final int BUTTON_HEIGHT = 20;
    /** Width of text input */
    private static final int TEXT_INPUT_WIDTH = 140;
    /** Height of text input */
    private static final int TEXT_INPUT_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    private static final int DONE_BUTTON_TOP_OFFSET = 26;
    /** Distance from top of the screen to this GUI's title */
    private static final int TITLE_HEIGHT = 8;
    /** Width of max power label */

    private TextFieldWidget textFieldMaxPower;

    private LoadTileEntity loadTileEntity;
    private PlayerEntity player;
    private boolean inService;
    private Watt resultPower;

    public LoadScreen(LoadTileEntity loadTileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.loadscreen"));
        this.loadTileEntity = loadTileEntity;
        this.player = player;
    }

    @Override
    protected void init() {
        inService = loadTileEntity.isInService();
        resultPower = loadTileEntity.getResultPower();
        textFieldMaxPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 50, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, loadTileEntity.getMaxPower().toString());
        textFieldMaxPower.setText(String.format("%f", loadTileEntity.getMaxPower().getMegaWatts()));
        textFieldMaxPower.setFocused2(true);
        textFieldMaxPower.setVisible(true);

        // Add the "Done" button
        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                // Text shown on the button
                I18n.format("gui.done"),
                // Action performed when the button is pressed
                button -> onClose()
        ));

        this.addButton(new Button(
            (this.width - BUTTON_WIDTH) / 2,
            75,
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
            }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        // First draw the background of the screen
        this.renderBackground();
        // Draw the title
        this.drawCenteredString(this.font, this.title.getFormattedText(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        // Draw max power label
        this.drawString(this.font, "Max Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 50 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 40, 50 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Power: " + String.format("%f", resultPower.getMegaWatts()) + " MW", (this.width / 2) - 20, 100, 0xFFFFFF);
        textFieldMaxPower.render(mouseX, mouseY, partialTicks);
        // Call the super class' method to complete rendering
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        if (textFieldMaxPower.isFocused()) {
            textFieldMaxPower.charTyped(p_charTyped_1_, p_charTyped_2_);
        }
        return super.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (textFieldMaxPower.isFocused()) {
            textFieldMaxPower.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (textFieldMaxPower.isFocused()) {
            textFieldMaxPower.keyReleased(keyCode, scanCode, modifiers);
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void onClose() {
        boolean shouldUpdate = true;
        double maxPower = 0;
        try {
            maxPower = Double.parseDouble(textFieldMaxPower.getText());
        } catch (NumberFormatException e) {
            shouldUpdate = false;
            PlayerUtils.error(player, "gui.electricblocks.err_invalid_number");
        }

        if (shouldUpdate) {
            loadTileEntity.setInService(inService);
            loadTileEntity.setMaxPower(new Watt(maxPower * 1000000));
            loadTileEntity.notifyUpdate();

            loadTileEntity.requestSimulation(player);
        }
        super.onClose();
    }

}
