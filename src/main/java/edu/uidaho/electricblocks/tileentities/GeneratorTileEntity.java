package edu.uidaho.electricblocks.tileentities;

import java.util.UUID;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.utils.ClientUtils;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;

public class GeneratorTileEntity extends SimulationTileEntity implements IMultimeter {

    private boolean slack = false;
    private MetricUnit maxPower = new MetricUnit(1000);
    private MetricUnit peakVoltage = new MetricUnit(1);
    private MetricUnit busVoltage = new MetricUnit(20, MetricUnit.MetricPrefix.KILO);
    private MetricUnit resultPower = new MetricUnit(0);
    private MetricUnit reactivePower = new MetricUnit(0);
    private MetricUnit resultVoltage = new MetricUnit(0);

    public GeneratorTileEntity() {
        super(RegistryHandler.GENERATOR_TILE_ENTITY.get(), SimulationType.GENERATOR);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putBoolean("slack", slack);
        compound.putDouble("maxPower", maxPower.get());
        compound.putDouble("peakVoltage", peakVoltage.get());
        compound.putDouble("busVoltage", busVoltage.get());
        compound.putDouble("resultPower", resultPower.get());
        compound.putDouble("reactivePower", reactivePower.get());
        compound.putDouble("resultVoltage", resultVoltage.get());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        slack = compound.getBoolean("slack");
        maxPower = new MetricUnit(compound.getDouble("maxPower"));
        peakVoltage = new MetricUnit(compound.getDouble("peakVoltage"));
        busVoltage = new MetricUnit(compound.getDouble("busVoltage"));
        resultPower = new MetricUnit(compound.getDouble("resultPower"));
        reactivePower = new MetricUnit(compound.getDouble("reactivePower"));
        resultVoltage = new MetricUnit(compound.getDouble("resultVoltage"));
        simId = compound.getUniqueId("simId");
    }

    @Override
    public void receiveSimulationResults(JsonObject results) {
        this.resultPower = new MetricUnit(results.get("p_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.reactivePower = new MetricUnit(results.get("q_mvar").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.resultVoltage = new MetricUnit(results.get("vm_pu").getAsDouble());
        notifyUpdate();
    }

    @Override
    public void zeroSim() {
        resultPower = new MetricUnit(0);
        reactivePower = new MetricUnit(0);
        resultVoltage = new MetricUnit(0);
        notifyUpdate();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonObject bus = getBusJson(busVoltage);
        UUID busId = embededBusses.get("main");

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("slack", slack);
        obj.addProperty("in_service", inService);
        obj.addProperty("bus", busId.toString());
        obj.addProperty("p_mw", maxPower.getMega());
        obj.addProperty("vm_pu", peakVoltage.get());

        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("main", UUID.randomUUID());
    }

    @Override
    public void fillPacketBuffer(double[] d) {
        d[0] = maxPower.get();
        d[1] = peakVoltage.get();
        d[2] = busVoltage.get();
    }

    @Override
    public int getNumInputs() {
        return 3;
    }

    public MetricUnit getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(MetricUnit maxPower) {
        this.maxPower = maxPower;
    }

    public MetricUnit getPeakVoltage() {
        return peakVoltage;
    }

    public void setPeakVoltage(MetricUnit peakVoltage) {
        this.peakVoltage = peakVoltage;
    }

    public MetricUnit getBusVoltage() {
        return busVoltage;
    }

    public void setBusVoltage(MetricUnit busVoltage) {
        this.busVoltage = busVoltage;
    }

    public boolean isSlack() {
        return slack;
    }

    public void setSlack(boolean slack) {
        this.slack = slack;
    }

    public MetricUnit getResultPower() {
        return resultPower;
    }

    public void setResultPower(MetricUnit resultPower) {
        this.resultPower = resultPower;
    }

    public MetricUnit getReactivePower() {
        return reactivePower;
    }

    public void setReactivePower(MetricUnit reactivePower) {
        this.reactivePower = reactivePower;
    }

    public MetricUnit getResultVoltage() {
        return resultVoltage;
    }

    public void setResultVoltage(MetricUnit resultVoltage) {
        this.resultVoltage = resultVoltage;
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        inService = !inService;
        notifyUpdate();
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientUtils.openGeneratorScreen(this, player));
    }

    @Override
    public void disable() {
        inService = false;
        peakVoltage = new MetricUnit(0);
        maxPower = new MetricUnit(0);
    }
    
}
