package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.network.ElectricBlocksPacketHandler;
import edu.uidaho.electricblocks.network.TileEntityMessageToServer;
import edu.uidaho.electricblocks.tileentities.ExternalGridTileEntity;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class ExternalGridScreen extends AbstractScreen {

    private TextFieldWidget textFieldSlackVoltage;
    private TextFieldWidget textFieldBusVoltage;
    private TextFieldWidget textFieldResultPower;
    private TextFieldWidget textFieldReactivePower;

    private ExternalGridTileEntity externalGridTileEntity;

    public ExternalGridScreen(ExternalGridTileEntity externalGridTileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.externalgridscreen"));
        this.externalGridTileEntity = externalGridTileEntity;
        this.player = player;
    }

    @Override
    protected void init() {
        inService = externalGridTileEntity.isInService();
        textFieldSlackVoltage = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldSlackVoltage.setText(String.format("%f", externalGridTileEntity.getSlackVoltage().get()));
        textFieldSlackVoltage.setFocused2(true);
        textFieldSlackVoltage.setVisible(true);
        addButton(textFieldSlackVoltage);
        setFocused(textFieldSlackVoltage);

        textFieldBusVoltage = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 55, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldBusVoltage.setText(String.format("%f", externalGridTileEntity.getBusVoltage().getKilo()));
        textFieldBusVoltage.setVisible(true);
        addButton(textFieldBusVoltage);

        textFieldResultPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldResultPower.setText(String.format("%f", externalGridTileEntity.getResultPower().getMega()));
        initializeResultField(textFieldResultPower);

        textFieldReactivePower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 120, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePower.setText(String.format("%f", externalGridTileEntity.getReactivePower().getMega()));
        initializeResultField(textFieldReactivePower);

        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        // Draw property labels
        this.drawString(this.font, "Slack Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Bus Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 55 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, 75 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 120 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "pu", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "kV", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 55 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MVar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 120 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw separator

    }

    @Override
    protected void submitChanges() {
        boolean shouldUpdate = true;
        double slackVoltage = 0, busVoltage = 0;
        try {
            slackVoltage = Double.parseDouble(textFieldSlackVoltage.getText());
            busVoltage = Double.parseDouble(textFieldBusVoltage.getText());
        } catch (NumberFormatException e) {
            shouldUpdate = false;
            PlayerUtils.error(player, "gui.electricblocks.err_invalid_number");
        }

        if (shouldUpdate) {
            PlayerUtils.sendMessage(player, "command.electricblocks.viewmodify.submit");
            externalGridTileEntity.setInService(inService);
            externalGridTileEntity.setSlackVoltage(new MetricUnit(slackVoltage));
            externalGridTileEntity.setBusVoltage(new MetricUnit(busVoltage, MetricUnit.MetricPrefix.KILO));
            TileEntityMessageToServer teMSG = new TileEntityMessageToServer(externalGridTileEntity, player);
            ElectricBlocksPacketHandler.INSTANCE.sendToServer(teMSG);
        }
    }

}
