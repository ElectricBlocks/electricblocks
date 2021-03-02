package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import edu.uidaho.electricblocks.utils.ClientUtils;
import edu.uidaho.electricblocks.utils.MetricUnit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import java.util.UUID;

public class BusTileEntity extends SimulationTileEntity implements IMultimeter {

    // Inputs
    private MetricUnit busVoltage = new MetricUnit(20, MetricUnit.MetricPrefix.KILO); // vn_kv

    // Outputs
    private double voltageMagnitude = 0; // vm_pu
    private double voltageAngle = 0; // va_degree
    private MetricUnit activePower = new MetricUnit(0); // p_mw
    private MetricUnit reactivePower = new MetricUnit(0); // q_mvar

    public BusTileEntity() {
        super(RegistryHandler.BUS_TILE_ENTITY.get(), SimulationType.BUS);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("busVoltage", busVoltage.get());

        compound.putDouble("voltageMagnitude", voltageMagnitude);
        compound.putDouble("voltageAngle", voltageAngle);
        compound.putDouble("activePower", activePower.get());
        compound.putDouble("reactivePower", reactivePower.get());
        return compound;
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        busVoltage = new MetricUnit(compound.getDouble("busVoltage"));

        voltageMagnitude = compound.getDouble("voltageMagnitude");
        voltageAngle = compound.getDouble("voltageAngle");
        activePower = new MetricUnit(compound.getDouble("activePower"));
        reactivePower = new MetricUnit(compound.getDouble("reactivePower"));
        simId = compound.getUniqueId("simId");
    }

    @Override
    public void receiveSimulationResults(JsonObject results) {
        this.voltageMagnitude = results.get("vm_pu").getAsDouble();
        this.voltageAngle = results.get("va_degree").getAsDouble();
        this.activePower = new MetricUnit(results.get("p_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.reactivePower = new MetricUnit(results.get("q_mvar").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        notifyUpdate();
    }

    @Override
    public void zeroSim() {
        voltageMagnitude = 0;
        voltageAngle = 0;
        activePower = new MetricUnit(0);
        reactivePower = new MetricUnit(0);
        notifyUpdate();
    }

    @Override
    public void disable() {
        inService = false;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("in_service", inService);
        obj.addProperty("vn_kv", busVoltage.getKilo());

        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void initEmbeddedBusses() {
        // No embedded busses!
    }

    // Overriding since there are no embedded buses
    @Override
    public UUID getEmbeddedBus(BlockPos pos) {
        if (getPos().manhattanDistance(pos) == 1) {
            return simId;
        }
        return null;
    }

    @Override
    public void fillPacketBuffer(double[] d) {
        d[0] = busVoltage.get();
    }

    @Override
    public int getNumInputs() {
        return 1;
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        inService = !inService;
        requestSimulation(player);
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientUtils.openBusScreen(this, player));
    }

    public MetricUnit getBusVoltage() {
        return busVoltage;
    }

    public void setBusVoltage(MetricUnit busVoltage) {
        this.busVoltage = busVoltage;
    }

    public double getVoltageMagnitude() {
        return voltageMagnitude;
    }

    public void setVoltageMagnitude(double voltageMagnitude) {
        this.voltageMagnitude = voltageMagnitude;
    }

    public double getVoltageAngle() {
        return voltageAngle;
    }

    public void setVoltageAngle(double voltageAngle) {
        this.voltageAngle = voltageAngle;
    }

    public MetricUnit getActivePower() {
        return activePower;
    }

    public void setActivePower(MetricUnit activePower) {
        this.activePower = activePower;
    }

    public MetricUnit getReactivePower() {
        return reactivePower;
    }

    public void setReactivePower(MetricUnit reactivePower) {
        this.reactivePower = reactivePower;
    }
}
