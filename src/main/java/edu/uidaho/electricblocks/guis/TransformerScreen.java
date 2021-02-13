package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.network.ElectricBlocksPacketHandler;
import edu.uidaho.electricblocks.network.TileEntityMessageToServer;
import edu.uidaho.electricblocks.tileentities.TransformerTileEntity;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class TransformerScreen extends AbstractScreen {

    // Constants
    private static final int MAX_SCROLL = 0;
    private static final int MIN_SCROLL = -420;
    private static final int SCROLL_CUTOFF_MIN = 20;

    // Layout elements
    TextFieldWidget textFieldRatedApparentPower; // sn_mva
    TextFieldWidget textFieldRatedVoltageAtHighBus; // vn_hv_kv
    TextFieldWidget textFieldRatedVoltageAtLowBus; // vn_lv_kv
    TextFieldWidget textFieldShortCircuitVoltagePercent; // vk_percent
    TextFieldWidget textFieldShortCircuitVoltageRealComponentPercent; // vkr_percent
    TextFieldWidget textFieldIronLosses; // pfw_kw
    TextFieldWidget textFieldOpenLoopLossesPercent; // i0_percent
    TextFieldWidget textFieldShiftDegree; // shift_degree

    TextFieldWidget textFieldPowerAtHighVoltageBus; // p_hv_mw
    TextFieldWidget textFieldReactivePowerAtHighVoltageBus; // q_hv_mvar
    TextFieldWidget textFieldPowerAtLowVoltageBus; // p_lv_mw
    TextFieldWidget textFieldReactivePowerAtLowVoltageBus; // q_lv_mvar
    TextFieldWidget textFieldActivePowerLosses; // pl_mw
    TextFieldWidget textFieldReactivePowerConsumption; // ql_mvar
    TextFieldWidget textFieldCurrentAtHighBus; // i_hv_ka
    TextFieldWidget textFieldCurrentAtLowBus; // i_lv_ka
    TextFieldWidget textFieldVoltageMagnitudeAtHighBus; // vm_hv_pu
    TextFieldWidget textFieldVoltageMagnitudeAtLowBus; // vm_lv_pu
    TextFieldWidget textFieldLoadingPercent; // loading_percent

    // Instance specific info
    private final TransformerTileEntity tileEntity;
    private int scrollPosition = 0;

    public TransformerScreen(TransformerTileEntity tileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.transformerscreen")
        .appendText(" ")
        .appendSibling(new TranslationTextComponent("gui.electricblocks.scrollable"))
        );
        this.tileEntity = tileEntity;
        this.player = player;
    }

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
    protected void init() {
        inService = tileEntity.isInService();

        // Inputs
        textFieldRatedApparentPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldRatedApparentPower.setText(String.format("%f", tileEntity.getRatedApparentPower().getMega()));
        textFieldRatedApparentPower.setFocused2(true);
        textFieldRatedApparentPower.setVisible(true);
        addButton(textFieldRatedApparentPower);
        setFocused(textFieldRatedApparentPower);

        textFieldRatedVoltageAtHighBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 55, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldRatedVoltageAtHighBus.setText(String.format("%f", tileEntity.getRatedVoltageAtHighBus().getKilo()));
        textFieldRatedVoltageAtHighBus.setVisible(true);
        addButton(textFieldRatedVoltageAtHighBus);

        textFieldRatedVoltageAtLowBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 85, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldRatedVoltageAtLowBus.setText(String.format("%f", tileEntity.getRatedVoltageAtLowBus().getKilo()));
        textFieldRatedVoltageAtLowBus.setVisible(true);
        addButton(textFieldRatedVoltageAtLowBus);

        textFieldShortCircuitVoltagePercent = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 115, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldShortCircuitVoltagePercent.setText(String.format("%f", tileEntity.getShortCircuitVoltagePercent()));
        textFieldShortCircuitVoltagePercent.setVisible(true);
        addButton(textFieldShortCircuitVoltagePercent);

        textFieldShortCircuitVoltageRealComponentPercent = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 145, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldShortCircuitVoltageRealComponentPercent.setText(String.format("%f", tileEntity.getShortCircuitVoltageRealComponentPercent()));
        textFieldShortCircuitVoltageRealComponentPercent.setVisible(true);
        addButton(textFieldShortCircuitVoltageRealComponentPercent);

        textFieldIronLosses = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 175, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldIronLosses.setText(String.format("%f", tileEntity.getIronLosses().getKilo()));
        textFieldIronLosses.setVisible(true);
        addButton(textFieldIronLosses);

        textFieldOpenLoopLossesPercent = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 205, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldOpenLoopLossesPercent.setText(String.format("%f", tileEntity.getOpenLoopLossesPercent()));
        textFieldOpenLoopLossesPercent.setVisible(true);
        addButton(textFieldOpenLoopLossesPercent);

        textFieldShiftDegree = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 235, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldShiftDegree.setText(String.format("%f", tileEntity.getShiftDegree()));
        textFieldShiftDegree.setVisible(true);
        addButton(textFieldShiftDegree);

        // Results
        textFieldPowerAtHighVoltageBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 270, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldPowerAtHighVoltageBus.setText(String.format("%f", tileEntity.getPowerAtHighVoltageBus().getMega()));
        initializeResultField(textFieldPowerAtHighVoltageBus);

        textFieldReactivePowerAtHighVoltageBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 300, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePowerAtHighVoltageBus.setText(String.format("%f", tileEntity.getReactivePowerAtHighVoltageBus().getMega()));
        initializeResultField(textFieldReactivePowerAtHighVoltageBus);

        textFieldPowerAtLowVoltageBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 330, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldPowerAtLowVoltageBus.setText(String.format("%f", tileEntity.getPowerAtLowVoltageBus().getMega()));
        initializeResultField(textFieldPowerAtLowVoltageBus);

        textFieldReactivePowerAtLowVoltageBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 360, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePowerAtLowVoltageBus.setText(String.format("%f", tileEntity.getReactivePowerAtLowVoltageBus().getMega()));
        initializeResultField(textFieldReactivePowerAtLowVoltageBus);

        textFieldActivePowerLosses = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 390, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldActivePowerLosses.setText(String.format("%f", tileEntity.getActivePowerLosses().getMega()));
        initializeResultField(textFieldActivePowerLosses);

        textFieldReactivePowerConsumption = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 420, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldReactivePowerConsumption.setText(String.format("%f", tileEntity.getReactivePowerConsumption().getMega()));
        initializeResultField(textFieldReactivePowerConsumption);

        textFieldCurrentAtHighBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 450, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldCurrentAtHighBus.setText(String.format("%f", tileEntity.getCurrentAtHighBus().getKilo()));
        initializeResultField(textFieldCurrentAtHighBus);

        textFieldCurrentAtLowBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 480, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldCurrentAtLowBus.setText(String.format("%f", tileEntity.getCurrentAtLowBus().getKilo()));
        initializeResultField(textFieldCurrentAtLowBus);

        textFieldVoltageMagnitudeAtHighBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 510, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldVoltageMagnitudeAtHighBus.setText(String.format("%f", tileEntity.getVoltageMagnitudeAtHighVoltageBus()));
        initializeResultField(textFieldVoltageMagnitudeAtHighBus);

        textFieldVoltageMagnitudeAtLowBus = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 540, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldVoltageMagnitudeAtLowBus.setText(String.format("%f", tileEntity.getVoltageMagnitudeAtLowVoltageBus()));
        initializeResultField(textFieldVoltageMagnitudeAtLowBus);

        textFieldLoadingPercent = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 570, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldLoadingPercent.setText(String.format("%f", tileEntity.getLoadingPercent()));
        initializeResultField(textFieldLoadingPercent);

        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        // Inputs
        this.drawString(this.font, "Apparent Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "High Bus Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 55 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Low Bus Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 85 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "S/C Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 115 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "S/C Voltage Real", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 145 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Iron Losses", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 175 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Open Loop Losses", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 205 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Shift Degree", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 235 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);

        // Results
        this.drawString(this.font, "HV Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 270 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "HV Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 300 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "LV Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 330 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "LV Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 360 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Power Losses", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 390 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "R Power Losses", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 420 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "HV Bus Current", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 450 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "LV Bus Current", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 480 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "HV Voltage MAG", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 510 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "LV Voltage MAG", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 540 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Loading Percent", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 570 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);

        // Input Units
        this.drawString(this.font, "MVA", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "kV", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 55 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "kV", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 85 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "%", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 115 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "%", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 145 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "kW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 175 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "%", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 205 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "degrees", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 235 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);

        // Output Units
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 270 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MVar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 300 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 330 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MVar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 360 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 390 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MVar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 420 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "kA", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 450 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "kA", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 480 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "pu", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 510 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "pu", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 540 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "%", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 570 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);

        // Draw separator
        int dividerHeight = scrollPosition + 255 + (this.font.FONT_HEIGHT / 2);
        if (dividerHeight >= SCROLL_CUTOFF_MIN) {
            this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, dividerHeight, 0xFFFFFF);
        }

        // Re-render buttons to ensure correct ordering
        this.doneButton.render(mouseX, mouseY, partialTicks);
        this.inServiceButton.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawString(FontRenderer p_drawString_1_, String p_drawString_2_, int p_drawString_3_, int p_drawString_4_, int p_drawString_5_) {
        if (p_drawString_4_ <= getMaxCutoff() && p_drawString_4_ >= SCROLL_CUTOFF_MIN)
            super.drawString(p_drawString_1_, p_drawString_2_, p_drawString_3_, p_drawString_4_, p_drawString_5_);
    }

    @Override
    protected void submitChanges() {
        boolean shouldUpdate = true;
        double apparentPower = 0, voltageAtHighBus = 0, voltageAtLowBus = 0, shortCircuitVoltagePercent = 0,
                shortCircuitVoltagePercentRealComponent = 0, ironLosses = 0, openLoopLossesPercent = 0, shiftDegree = 0;
        try {
            apparentPower = Double.parseDouble(textFieldRatedApparentPower.getText());
            voltageAtHighBus = Double.parseDouble(textFieldRatedVoltageAtHighBus.getText());
            voltageAtLowBus = Double.parseDouble(textFieldRatedVoltageAtLowBus.getText());
            shortCircuitVoltagePercent = Double.parseDouble(textFieldShortCircuitVoltagePercent.getText());
            shortCircuitVoltagePercentRealComponent = Double.parseDouble(textFieldShortCircuitVoltageRealComponentPercent.getText());
            ironLosses = Double.parseDouble(textFieldIronLosses.getText());
            openLoopLossesPercent = Double.parseDouble(textFieldOpenLoopLossesPercent.getText());
            shiftDegree = Double.parseDouble(textFieldShiftDegree.getText());
        } catch (NumberFormatException e) {
            shouldUpdate = false;
            PlayerUtils.error(player, "gui.electricblocks.err_invalid_number");
        }

        if (shouldUpdate) {
            PlayerUtils.sendMessage(player, "command.electricblocks.viewmodify.submit");
            tileEntity.setInService(inService);
            tileEntity.setRatedApparentPower(new MetricUnit(apparentPower, MetricUnit.MetricPrefix.MEGA));
            tileEntity.setRatedVoltageAtHighBus(new MetricUnit(voltageAtHighBus, MetricUnit.MetricPrefix.KILO));
            tileEntity.setRatedVoltageAtLowBus(new MetricUnit(voltageAtLowBus, MetricUnit.MetricPrefix.KILO));
            tileEntity.setShortCircuitVoltagePercent(shortCircuitVoltagePercent);
            tileEntity.setShortCircuitVoltageRealComponentPercent(shortCircuitVoltagePercentRealComponent);
            tileEntity.setIronLosses(new MetricUnit(ironLosses, MetricUnit.MetricPrefix.KILO));
            tileEntity.setOpenLoopLossesPercent(openLoopLossesPercent);
            tileEntity.setShiftDegree(shiftDegree);
            TileEntityMessageToServer teMSG = new TileEntityMessageToServer(tileEntity, player);
            ElectricBlocksPacketHandler.INSTANCE.sendToServer(teMSG);
        }
    }

    public int getMaxCutoff() {
        return inServiceButton.y - BUTTON_HEIGHT;
    }
    
}
