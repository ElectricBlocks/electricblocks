package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.guis.TransformerScreen;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class TransformerTileEntity extends SimulationTileEntity implements IMultimeter {

    // Inputs
    private boolean inService = false;
    private MetricUnit ratedApparentPower = new MetricUnit(40000000); // sn_mva
    private MetricUnit ratedVoltageAtHighBus = new MetricUnit(110000); // vn_hv_kv
    private MetricUnit ratedVoltageAtLowBus = new MetricUnit(10000); // vn_lv_kv
    private double shortCircuitVoltagePercent = 10.0; // vk_percent
    private double shortCircuitVoltageRealComponentPercent = 0.3; // vkr_percent
    private MetricUnit ironLosses = new MetricUnit(30000); // pfw_kw
    private double openLoopLossesPercent = 0.1; // i0_percent
    private double shiftDegree = 30.0; // shift_degree

    // Outputs
    private MetricUnit powerAtHighVoltageBus = new MetricUnit(0); // p_hv_mw
    private MetricUnit reactivePowerAtHighVoltageBus = new MetricUnit(0); // q_hv_mvar
    private MetricUnit powerAtLowVoltageBus = new MetricUnit(0); // p_lv_mw
    private MetricUnit reactivePowerAtLowVoltageBus = new MetricUnit(0); // q_lv_mw
    private MetricUnit activePowerLosses = new MetricUnit(0); // pl_mw
    private MetricUnit reactivePowerConsumption = new MetricUnit(0); // ql_mvar
    private MetricUnit voltageAtHighBus = new MetricUnit(0); // i_hv_ka
    private MetricUnit voltageAtLowBus = new MetricUnit(0); // i_lv_ka

    public TransformerTileEntity() {
        super(RegistryHandler.TRANSFORMER_TILE_ENTITY.get(), SimulationType.TRANSFORMER);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.putBoolean("inService", inService);
        compound.putDouble("ratedApparentPower", ratedApparentPower.get());
        compound.putDouble("ratedVoltageAtHighBus", ratedVoltageAtHighBus.get());
        compound.putDouble("ratedVoltageAtLowBus", ratedVoltageAtLowBus.get());
        compound.putDouble("shortCircuitVoltagePercent", shortCircuitVoltagePercent);
        compound.putDouble("shortCircuitVoltageRealComponentPercent", shortCircuitVoltageRealComponentPercent);
        compound.putDouble("ironLosses", ironLosses.get());
        compound.putDouble("openLoopLossesPercent", openLoopLossesPercent);
        compound.putDouble("shiftDegree", shiftDegree);
        return super.write(compound);
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        inService = compound.getBoolean("inService");
        ratedApparentPower = new MetricUnit(compound.getDouble("ratedApparentPower"));
        ratedVoltageAtHighBus = new MetricUnit(compound.getDouble("ratedVoltageAtHighBus"));
        ratedVoltageAtLowBus = new MetricUnit(compound.getDouble("ratedVoltageAtLowBus"));
        shortCircuitVoltagePercent = compound.getDouble("shortCircuitVoltagePercent");
        shortCircuitVoltageRealComponentPercent = compound.getDouble("shortCircuitVoltageRealComponentPercent");
        ironLosses = new MetricUnit(compound.getDouble("ironLosses"));
        openLoopLossesPercent = compound.getDouble("openLoopLossesPercent");
        shiftDegree = compound.getDouble("shiftDegree");
        super.read(compound);
    }

    @Override
    public void receiveSimulationResults(JsonObject jsonObject) {
        // TODO Auto-generated method stub

    }

    @Override
    public void zeroSim() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub

    }

    @Override
    public JsonObject toJson() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void initEmbeddedBusses() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        Minecraft.getInstance().displayGuiScreen(new TransformerScreen(this, player));
    }

    public boolean getInService() {
        return inService;
    }

    public MetricUnit getRatedApparentPower() {
        return ratedApparentPower;
    }

    public void setRatedApparentPower(MetricUnit ratedApparentPower) {
        this.ratedApparentPower = ratedApparentPower;
    }
}
