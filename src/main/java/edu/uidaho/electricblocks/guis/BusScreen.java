package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.network.ElectricBlocksPacketHandler;
import edu.uidaho.electricblocks.network.TileEntityMessageToServer;
import edu.uidaho.electricblocks.tileentities.BusTileEntity;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.LogicalSide;

public class BusScreen extends AbstractScreen {

    private TextFieldWidget textFieldBusVoltage;

    private TextFieldWidget textFieldVoltageMagnitude;
    private TextFieldWidget textFieldVoltageAngle;
    private TextFieldWidget textFieldActivePower;
    private TextFieldWidget textFieldReactivePower;

    private final BusTileEntity busTileEntity;

    public BusScreen(BusTileEntity busTileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.busscreen"));
        this.busTileEntity = busTileEntity;
        this.player = player;
    }

    @Override
    protected void init() {
        inService = busTileEntity.isInService();
        textFieldBusVoltage = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldBusVoltage.setText(String.format("%f", busTileEntity.getBusVoltage().getKilo()));
        textFieldBusVoltage.setFocused2(true);
        textFieldBusVoltage.setVisible(true);
        addButton(textFieldBusVoltage);
        setFocused(textFieldBusVoltage);

        textFieldVoltageMagnitude = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 60, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldVoltageMagnitude.setText(String.format("%f", busTileEntity.getVoltageMagnitude()));
        initializeResultField(textFieldVoltageMagnitude);

        textFieldVoltageAngle = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldVoltageAngle.setText(String.format("%f", busTileEntity.getVoltageAngle()));
        initializeResultField(textFieldVoltageAngle);

        textFieldActivePower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 120, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldActivePower.setText(String.format("%f", busTileEntity.getActivePower().getMega()));
        initializeResultField(textFieldActivePower);

        textFieldReactivePower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 150, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePower.setText(String.format("%f", busTileEntity.getReactivePower().getMega()));
        initializeResultField(textFieldReactivePower);

        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        // Draw property labels
        this.drawString(this.font, "Bus Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, 45 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Voltage Magnitude", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Voltage Angle", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 120 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 150 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "kV", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "pu", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "degrees", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 120 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MVar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, 150 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw separator
    }

    @Override
    protected void submitChanges() {
        boolean shouldUpdate = true;
        double busVoltage = 0;
        try {
            busVoltage = Double.parseDouble(textFieldBusVoltage.getText());
        } catch (NumberFormatException e) {
            shouldUpdate = false;
            PlayerUtils.error(player, LogicalSide.CLIENT, "gui.electricblocks.err_invalid_number");
        }

        if (shouldUpdate) {
            PlayerUtils.sendMessage(player, LogicalSide.CLIENT, "command.electricblocks.viewmodify.submit");
            busTileEntity.setInService(inService);
            busTileEntity.setBusVoltage(new MetricUnit(busVoltage, MetricUnit.MetricPrefix.KILO));
            TileEntityMessageToServer teMSG = new TileEntityMessageToServer(busTileEntity, player);
            ElectricBlocksPacketHandler.INSTANCE.sendToServer(teMSG);
        }
    }
}
