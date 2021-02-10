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
import java.util.UUID;

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
    private MetricUnit currentAtHighBus = new MetricUnit(0); // i_hv_ka
    private MetricUnit currentAtLowBus = new MetricUnit(0); // i_lv_ka
    private double voltageMagnitudeAtHighVoltageBus = 0.0; // vm_hv_pu
    private double voltageMagnitudeAtLowVoltageBus = 0.0;
    private double loadingPercent = 0.0; // loading_percent

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

        compound.putDouble("powerAtHighVoltageBus", powerAtHighVoltageBus.get());
        compound.putDouble("reactivePowerAtHighVoltageBus", reactivePowerAtHighVoltageBus.get());
        compound.putDouble("powerAtLowVoltageBus", powerAtLowVoltageBus.get());
        compound.putDouble("reactivePowerAtLowVoltageBus", reactivePowerAtLowVoltageBus.get());
        compound.putDouble("activePowerLosses", activePowerLosses.get());
        compound.putDouble("reactivePowerConsumption", reactivePowerConsumption.get());
        compound.putDouble("currentAtHighBus", currentAtHighBus.get());
        compound.putDouble("currentAtLowBus", currentAtLowBus.get());
        compound.putDouble("voltageMagnitudeAtHighVoltageBus", voltageMagnitudeAtHighVoltageBus);
        compound.putDouble("voltageMagnitudeAtLowVoltageBus", voltageMagnitudeAtLowVoltageBus);
        compound.putDouble("loadingPercent", loadingPercent);
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

        powerAtHighVoltageBus = new MetricUnit(compound.getDouble("powerAtHighVoltageBus")); // p_hv_mw
        reactivePowerAtHighVoltageBus = new MetricUnit(compound.getDouble("reactivePowerAtHighVoltageBus")); // q_hv_mvar
        powerAtLowVoltageBus = new MetricUnit(compound.getDouble("powerAtLowVoltageBus")); // p_lv_mw
        reactivePowerAtLowVoltageBus = new MetricUnit(compound.getDouble("reactivePowerAtLowVoltageBus")); // q_lv_mw
        activePowerLosses = new MetricUnit(compound.getDouble("activePowerLosses")); // pl_mw
        reactivePowerConsumption = new MetricUnit(compound.getDouble("reactivePowerConsumption")); // ql_mvar
        currentAtHighBus = new MetricUnit(compound.getDouble("currentAtHighBus")); // i_hv_ka
        currentAtLowBus = new MetricUnit(compound.getDouble("currentAtLowBus")); // i_lv_ka
        voltageMagnitudeAtHighVoltageBus = compound.getDouble("voltageMagnitudeAtHighVoltageBus"); // vm_hv_pu
        voltageMagnitudeAtLowVoltageBus = compound.getDouble("voltageMagnitudeAtLowVoltageBus");
        loadingPercent = compound.getDouble("loadingPercent"); // loading_percent
        super.read(compound);
    }

    @Override
    public void receiveSimulationResults(JsonObject jsonObject) {
        this.powerAtHighVoltageBus = new MetricUnit(jsonObject.get("p_hv_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.reactivePowerAtHighVoltageBus = new MetricUnit(jsonObject.get("q_hv_mvar").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.powerAtLowVoltageBus = new MetricUnit(jsonObject.get("p_lv_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.reactivePowerAtLowVoltageBus = new MetricUnit(jsonObject.get("q_lv_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.activePowerLosses = new MetricUnit(jsonObject.get("pl_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.reactivePowerConsumption = new MetricUnit(jsonObject.get("ql_mvar").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.currentAtHighBus = new MetricUnit(jsonObject.get("i_hv_ka").getAsDouble(), MetricUnit.MetricPrefix.KILO);
        this.currentAtLowBus = new MetricUnit(jsonObject.get("i_lv_ka").getAsDouble(), MetricUnit.MetricPrefix.KILO);
        this.voltageMagnitudeAtHighVoltageBus = jsonObject.get("vm_hv_pu").getAsDouble();
        this.voltageMagnitudeAtLowVoltageBus = jsonObject.get("vm_lv_pu").getAsDouble();
        this.loadingPercent = jsonObject.get("loading_percent").getAsDouble();
        notifyUpdate();
    }

    @Override
    public void zeroSim() {
        powerAtHighVoltageBus = new MetricUnit(0); // p_hv_mw
        reactivePowerAtHighVoltageBus = new MetricUnit(0); // q_hv_mvar
        powerAtLowVoltageBus = new MetricUnit(0); // p_lv_mw
        reactivePowerAtLowVoltageBus = new MetricUnit(0); // q_lv_mw
        activePowerLosses = new MetricUnit(0); // pl_mw
        reactivePowerConsumption = new MetricUnit(0); // ql_mvar
        currentAtHighBus = new MetricUnit(0); // i_hv_ka
        currentAtLowBus = new MetricUnit(0); // i_lv_ka
        voltageMagnitudeAtHighVoltageBus = 0.0; // vm_hv_pu
        voltageMagnitudeAtLowVoltageBus = 0.0;
        loadingPercent = 0.0; // loading_percent
    }

    @Override
    public void disable() {
        inService = false;
        ratedApparentPower = new MetricUnit(0);
    }

    @Override
    public JsonObject toJson() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("lowVoltage", UUID.randomUUID());
        embededBusses.put("highVoltage", UUID.randomUUID());
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        inService = !inService;
        requestSimulation(player);
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
