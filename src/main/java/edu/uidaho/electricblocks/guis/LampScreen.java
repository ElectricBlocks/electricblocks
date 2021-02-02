package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.tileentities.LampTileEntity;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class LampScreen extends AbstractScreen {

    // Layout elements
    private TextFieldWidget textFieldMaxPower;
    private TextFieldWidget textFieldResultPower;
    private TextFieldWidget textFieldReactivePower;
    private TextFieldWidget textFieldLight;

    // Info needed to preload the form with data
    private LampTileEntity lampTileEntity;
    private boolean inService;

    public LampScreen(LampTileEntity lampTileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.lampscreen"));
        this.lampTileEntity = lampTileEntity;
        this.player = player;
    }

    @Override
    protected void init() {
        inService = lampTileEntity.isInService();
        textFieldMaxPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldMaxPower.setText(String.format("%f", lampTileEntity.getMaxPower().getMegaWatts()));
        textFieldMaxPower.setFocused2(true);
        textFieldMaxPower.setVisible(true);
        addButton(textFieldMaxPower);
        setFocused(textFieldMaxPower);

        textFieldResultPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 60, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldResultPower.setText(String.format("%f", lampTileEntity.getResultPower().getMegaWatts()));
        textFieldResultPower.setVisible(true);
        textFieldResultPower.setEnabled(false);
        textFieldResultPower.setDisabledTextColour(enabledColor);
        addButton(textFieldResultPower);

        textFieldReactivePower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePower.setText(String.format("%f", lampTileEntity.getReactivePower().getMegaWatts()));
        textFieldReactivePower.setVisible(true);
        textFieldReactivePower.setEnabled(false);
        textFieldReactivePower.setDisabledTextColour(enabledColor);
        addButton(textFieldReactivePower);

        textFieldLight = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePower.setText(String.format("%f", lampTileEntity.getReactivePower().getMegaWatts()));
        textFieldReactivePower.setVisible(true);
        textFieldReactivePower.setEnabled(false);
        textFieldReactivePower.setDisabledTextColour(enabledColor);
        addButton(textFieldReactivePower);

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
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        // Draw property labels
        this.drawString(this.font, "Max Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Mvar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw separator
        this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, 45 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
    }

    @Override
    protected void submitChanges() {
        boolean shouldUpdate = true;
        double maxPower = 0;
        try {
            maxPower = Double.parseDouble(textFieldMaxPower.getText());
        } catch (NumberFormatException e) {
            shouldUpdate = false;
            PlayerUtils.error(player, "gui.electricblocks.err_invalid_number");
        }

        if (shouldUpdate) {
            PlayerUtils.sendMessage(player, "command.electricblocks.viewmodify.submit");
            lampTileEntity.setInService(inService);
            lampTileEntity.setMaxPower(new Watt(maxPower * 1000000));
            lampTileEntity.notifyUpdate();

            lampTileEntity.requestSimulation(player);
        }
    }

}
