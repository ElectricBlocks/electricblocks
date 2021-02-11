package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.tileentities.TransformerTileEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class TransformerScreen extends AbstractScreen {

    // Constants
    private static final int MAX_SCROLL = 0;
    private static final int MIN_SCROLL = -250;
    private static final int SCROLL_VISIBILITY_CUTOFF = 20;

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
                    w.visible = w.y >= SCROLL_VISIBILITY_CUTOFF;
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollAmount);
    }

    @Override
    protected void init() {
        inService = tileEntity.isInService();

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

        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        this.drawString(this.font, "Apparent Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "High Bus Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 55 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Low Bus Voltage", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 85 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "MVA", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "kV", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 55 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "kV", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 85 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw separator
        int dividerHeight = scrollPosition + 255 + (this.font.FONT_HEIGHT / 2);
        if (dividerHeight >= SCROLL_VISIBILITY_CUTOFF) {
            this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, dividerHeight, 0xFFFFFF);
        }

        // Re-render buttons to ensure correct ordering
        this.doneButton.render(mouseX, mouseY, partialTicks);
        this.inServiceButton.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawString(FontRenderer p_drawString_1_, String p_drawString_2_, int p_drawString_3_, int p_drawString_4_, int p_drawString_5_) {
        if (p_drawString_4_ >= SCROLL_VISIBILITY_CUTOFF)
            super.drawString(p_drawString_1_, p_drawString_2_, p_drawString_3_, p_drawString_4_, p_drawString_5_);
    }

    @Override
    protected void submitChanges() {

    }
    
}
