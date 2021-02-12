package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.blocks.TransformerBlock;
import edu.uidaho.electricblocks.utils.ClientUtils;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.guis.TransformerScreen;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

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
    private MetricUnit reactivePowerAtLowVoltageBus = new MetricUnit(0); // q_lv_mvar
    private MetricUnit activePowerLosses = new MetricUnit(0); // pl_mw
    private MetricUnit reactivePowerConsumption = new MetricUnit(0); // ql_mvar
    private MetricUnit currentAtHighBus = new MetricUnit(0); // i_hv_ka
    private MetricUnit currentAtLowBus = new MetricUnit(0); // i_lv_ka
    private double voltageMagnitudeAtHighVoltageBus = 0.0; // vm_hv_pu
    private double voltageMagnitudeAtLowVoltageBus = 0.0;
    private double loadingPercent = 0.0; // loading_percent

    private Direction cachedDirection = Direction.NORTH; // Cache direction for when block is broken

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
        this.reactivePowerAtLowVoltageBus = new MetricUnit(jsonObject.get("q_lv_mvar").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
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
        JsonObject json = new JsonObject();

        JsonObject lvBus = getBusJson();
        UUID lvBusId = embededBusses.get("lowVoltage");

        JsonObject hvBus = getBusJson();
        UUID hvBusId = embededBusses.get("highVoltage");

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("in_service", inService);
        obj.addProperty("hv_bus", hvBusId.toString());
        obj.addProperty("lv_bus", lvBusId.toString());
        obj.addProperty("sn_mva", ratedApparentPower.getMega());
        obj.addProperty("vn_hv_kv", ratedVoltageAtHighBus.getKilo());
        obj.addProperty("vn_lv_kv", ratedVoltageAtLowBus.getKilo());
        obj.addProperty("vk_percent", shortCircuitVoltagePercent);
        obj.addProperty("vkr_percent", shortCircuitVoltageRealComponentPercent);
        obj.addProperty("pfe_kw", ironLosses.getKilo());
        obj.addProperty("i0_percent", openLoopLossesPercent);
        obj.addProperty("shift_degree", shiftDegree);



        json.add(lvBusId.toString(), lvBus);
        json.add(hvBusId.toString(), hvBus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("lowVoltage", UUID.randomUUID());
        embededBusses.put("highVoltage", UUID.randomUUID());
    }

    @Override
    public UUID getEmbeddedBus(BlockPos pos) {
        if (getPos().manhattanDistance(pos) == 1) {
            Direction d;
            try {
                d = getBlockState().get(TransformerBlock.FACING);
                cachedDirection = d;
            } catch (IllegalArgumentException e) {
                d = cachedDirection;
            }
            if (getPos().offset(d).equals(pos)) {
                return embededBusses.get("highVoltage");
            }
            if (getPos().offset(d.getOpposite()).equals(pos)) {
                return embededBusses.get("lowVoltage");
            }
        }
        return null;
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        inService = !inService;
        requestSimulation(player);
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientUtils.openTransformerScreen(this, player));
    }

    public MetricUnit getRatedApparentPower() {
        return ratedApparentPower;
    }

    public void setRatedApparentPower(MetricUnit ratedApparentPower) {
        this.ratedApparentPower = ratedApparentPower;
    }

    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    @Override
    public void fillPacketBuffer(double[] d) {
        d[0] = getRatedApparentPower().get();
        d[1] = getRatedVoltageAtHighBus().get();
        d[2] = getRatedVoltageAtLowBus().get();
        d[3] = getShortCircuitVoltagePercent();
        d[4] = getShortCircuitVoltageRealComponentPercent();
        d[5] = getIronLosses().get();
        d[6] = getOpenLoopLossesPercent();
        d[7] = getShiftDegree();
    }

    @Override
    public int getNumInputs() {
        return 8;
    }

    public MetricUnit getRatedVoltageAtHighBus() {
        return ratedVoltageAtHighBus;
    }

    public void setRatedVoltageAtHighBus(MetricUnit ratedVoltageAtHighBus) {
        this.ratedVoltageAtHighBus = ratedVoltageAtHighBus;
    }

    public MetricUnit getRatedVoltageAtLowBus() {
        return ratedVoltageAtLowBus;
    }

    public void setRatedVoltageAtLowBus(MetricUnit ratedVoltageAtLowBus) {
        this.ratedVoltageAtLowBus = ratedVoltageAtLowBus;
    }

    public double getShortCircuitVoltagePercent() {
        return shortCircuitVoltagePercent;
    }

    public void setShortCircuitVoltagePercent(double shortCircuitVoltagePercent) {
        this.shortCircuitVoltagePercent = shortCircuitVoltagePercent;
    }

    public double getShortCircuitVoltageRealComponentPercent() {
        return shortCircuitVoltageRealComponentPercent;
    }

    public void setShortCircuitVoltageRealComponentPercent(double shortCircuitVoltageRealComponentPercent) {
        this.shortCircuitVoltageRealComponentPercent = shortCircuitVoltageRealComponentPercent;
    }

    public MetricUnit getIronLosses() {
        return ironLosses;
    }

    public void setIronLosses(MetricUnit ironLosses) {
        this.ironLosses = ironLosses;
    }

    public double getOpenLoopLossesPercent() {
        return openLoopLossesPercent;
    }

    public void setOpenLoopLossesPercent(double openLoopLossesPercent) {
        this.openLoopLossesPercent = openLoopLossesPercent;
    }

    public double getShiftDegree() {
        return shiftDegree;
    }

    public void setShiftDegree(double shiftDegree) {
        this.shiftDegree = shiftDegree;
    }

    public MetricUnit getPowerAtHighVoltageBus() {
        return powerAtHighVoltageBus;
    }

    public void setPowerAtHighVoltageBus(MetricUnit powerAtHighVoltageBus) {
        this.powerAtHighVoltageBus = powerAtHighVoltageBus;
    }

    public MetricUnit getReactivePowerAtHighVoltageBus() {
        return reactivePowerAtHighVoltageBus;
    }

    public void setReactivePowerAtHighVoltageBus(MetricUnit reactivePowerAtHighVoltageBus) {
        this.reactivePowerAtHighVoltageBus = reactivePowerAtHighVoltageBus;
    }

    public MetricUnit getPowerAtLowVoltageBus() {
        return powerAtLowVoltageBus;
    }

    public void setPowerAtLowVoltageBus(MetricUnit powerAtLowVoltageBus) {
        this.powerAtLowVoltageBus = powerAtLowVoltageBus;
    }

    public MetricUnit getReactivePowerAtLowVoltageBus() {
        return reactivePowerAtLowVoltageBus;
    }

    public void setReactivePowerAtLowVoltageBus(MetricUnit reactivePowerAtLowVoltageBus) {
        this.reactivePowerAtLowVoltageBus = reactivePowerAtLowVoltageBus;
    }

    public MetricUnit getActivePowerLosses() {
        return activePowerLosses;
    }

    public void setActivePowerLosses(MetricUnit activePowerLosses) {
        this.activePowerLosses = activePowerLosses;
    }

    public MetricUnit getReactivePowerConsumption() {
        return reactivePowerConsumption;
    }

    public void setReactivePowerConsumption(MetricUnit reactivePowerConsumption) {
        this.reactivePowerConsumption = reactivePowerConsumption;
    }

    public MetricUnit getCurrentAtHighBus() {
        return currentAtHighBus;
    }

    public void setCurrentAtHighBus(MetricUnit currentAtHighBus) {
        this.currentAtHighBus = currentAtHighBus;
    }

    public MetricUnit getCurrentAtLowBus() {
        return currentAtLowBus;
    }

    public void setCurrentAtLowBus(MetricUnit currentAtLowBus) {
        this.currentAtLowBus = currentAtLowBus;
    }

    public double getVoltageMagnitudeAtHighVoltageBus() {
        return voltageMagnitudeAtHighVoltageBus;
    }

    public void setVoltageMagnitudeAtHighVoltageBus(double voltageMagnitudeAtHighVoltageBus) {
        this.voltageMagnitudeAtHighVoltageBus = voltageMagnitudeAtHighVoltageBus;
    }

    public double getVoltageMagnitudeAtLowVoltageBus() {
        return voltageMagnitudeAtLowVoltageBus;
    }

    public void setVoltageMagnitudeAtLowVoltageBus(double voltageMagnitudeAtLowVoltageBus) {
        this.voltageMagnitudeAtLowVoltageBus = voltageMagnitudeAtLowVoltageBus;
    }

    public double getLoadingPercent() {
        return loadingPercent;
    }

    public void setLoadingPercent(double loadingPercent) {
        this.loadingPercent = loadingPercent;
    }
}
