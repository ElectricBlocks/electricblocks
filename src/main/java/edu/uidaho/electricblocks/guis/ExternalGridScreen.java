package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.tileentities.ExternalGridTileEntity;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class ExternalGridScreen extends AbstractScreen {

    private TextFieldWidget textFieldVoltage;
    private TextFieldWidget textFieldResultPower;
    private TextFieldWidget textFieldReactivePower;

    private ExternalGridTileEntity externalGridTileEntity;
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
        textFieldVoltage.setText(String.format("%f", externalGridTileEntity.getVoltage().get()));
        textFieldVoltage.setFocused2(true);
        textFieldVoltage.setVisible(true);
        addButton(textFieldVoltage);

        textFieldResultPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 60, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldResultPower.setText(String.format("%f", externalGridTileEntity.getResultPower().getMega()));
        textFieldResultPower.setVisible(true);
        textFieldResultPower.setEnabled(false);
        textFieldResultPower.setDisabledTextColour(enabledColor);
        addButton(textFieldResultPower);

        textFieldReactivePower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePower.setText(String.format("%f", externalGridTileEntity.getReactivePower().getMega()));
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
        this.drawString(this.font, "Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "V", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MVar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw separator
        this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, 45 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
    }

    @Override
    protected void submitChanges() {
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
            externalGridTileEntity.setVoltage(new MetricUnit(voltage));
            externalGridTileEntity.notifyUpdate();

            externalGridTileEntity.requestSimulation(player);
        }
    }

}
