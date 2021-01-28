package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.electric.Volt;
import edu.uidaho.electricblocks.tileentities.GeneratorTileEntity;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class GeneratorScreen extends AbstractScreen {

    // Layout elements
    private TextFieldWidget textFieldMaxPower;
    private TextFieldWidget textFieldVoltage;
    private TextFieldWidget textFieldResultPower;
    private TextFieldWidget textFieldReactivePower;
    private TextFieldWidget textFieldResultVoltage;

    // Info needed to preload the form with data
    private GeneratorTileEntity genTileEntity;
    private boolean inService;

    public GeneratorScreen(GeneratorTileEntity genTileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.generatorscreen"));
        this.genTileEntity = genTileEntity;
        this.player = player;
    }

    @Override
    protected void init() {
        inService = genTileEntity.isInService();
        textFieldMaxPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldMaxPower.setText(String.format("%f", genTileEntity.getMaxPower().getMegaWatts()));
        textFieldMaxPower.setFocused2(true);
        textFieldMaxPower.setVisible(true);
        this.addButton(textFieldMaxPower);
        this.setFocused(textFieldMaxPower);

        textFieldVoltage = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 55, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldVoltage.setText(String.format("%f", genTileEntity.getNominalVoltage().getVolts()));
        textFieldVoltage.setVisible(true);
        this.addButton(textFieldVoltage);

        textFieldResultPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldResultPower.setText(String.format("%f", genTileEntity.getResultPower().getMegaWatts()));
        textFieldResultPower.setVisible(true);
        textFieldResultPower.setEnabled(false);
        textFieldResultPower.setDisabledTextColour(enabledColor);
        this.addButton(textFieldResultPower);

        textFieldReactivePower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 120, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePower.setText(String.format("%f", genTileEntity.getReactivePower().getMegaWatts()));
        textFieldReactivePower.setVisible(true);
        textFieldReactivePower.setEnabled(false);
        textFieldReactivePower.setDisabledTextColour(enabledColor);
        this.addButton(textFieldReactivePower);

        textFieldResultVoltage = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 150, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldResultVoltage.setText(String.format("%f", genTileEntity.getReactivePower().getMegaWatts()));
        textFieldResultVoltage.setVisible(true);
        textFieldResultVoltage.setEnabled(false);
        textFieldResultVoltage.setDisabledTextColour(enabledColor);
        this.addButton(textFieldResultVoltage);

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
        this.drawString(this.font, "Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 55 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 120 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 150 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "V", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 55 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Mvar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 120 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "V", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 150 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw separator
        this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, 75 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
    }

    @Override
    protected void submitChanges() {
        boolean shouldUpdate = true;
        double maxPower = 0;
        double voltage = 0;
        try {
            maxPower = Double.parseDouble(textFieldMaxPower.getText());
            voltage = Double.parseDouble(textFieldVoltage.getText());
        } catch (NumberFormatException e) {
            shouldUpdate = false;
            PlayerUtils.error(player, "gui.electricblocks.err_invalid_number");
        }

        if (shouldUpdate) {
            PlayerUtils.sendMessage(player, "command.electricblocks.viewmodify.submit");
            genTileEntity.setInService(inService);
            genTileEntity.setMaxPower(new Watt(maxPower * 1000000));
            genTileEntity.setNominalVoltage(new Volt(voltage));
            genTileEntity.notifyUpdate();

            genTileEntity.requestSimulation(player);
        }
    }
    
}