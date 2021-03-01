package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.tileentities.BusTileEntity;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class BusScreen extends AbstractScreen {

    private TextFieldWidget textFieldBusVoltage;

    private TextFieldWidget textFieldVoltageMagnitude;
    private TextFieldWidget textFieldVoltageAngle;
    private TextFieldWidget textFieldActivePower;
    private TextFieldWidget textFieldReactivePower;

    private BusTileEntity busTileEntity;

    public BusScreen(BusTileEntity busTileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.busscreen"));
        this.busTileEntity = busTileEntity;
        this.player = player;
    }

    @Override
    protected void init() {
        inService = busTileEntity.isInService();


        super.init();
    }

    @Override
    protected void submitChanges() {

    }
}
