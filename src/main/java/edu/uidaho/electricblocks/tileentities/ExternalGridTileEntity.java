package edu.uidaho.electricblocks.tileentities;

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
import java.util.UUID;

public class ExternalGridTileEntity extends SimulationTileEntity implements IMultimeter {

    private MetricUnit slackVoltage = new MetricUnit(1);
    private MetricUnit busVoltage = new MetricUnit(20, MetricUnit.MetricPrefix.KILO);
    private MetricUnit resultPower = new MetricUnit(0);
    private MetricUnit reactivePower = new MetricUnit(0);

    public ExternalGridTileEntity() {
        super(RegistryHandler.EXTERNAL_GRID_TILE_ENTITY.get(), SimulationType.EXT_GRID);
    }


    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("slackVoltage", slackVoltage.get());
        compound.putDouble("busVoltage", busVoltage.get());
        compound.putDouble("resultPower", resultPower.get());
        compound.putDouble("reactivePower", reactivePower.get());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        slackVoltage = new MetricUnit(compound.getDouble("slackVoltage"));
        busVoltage = new MetricUnit(compound.getDouble("busVoltage"));
        resultPower = new MetricUnit(compound.getDouble("resultPower"));
        reactivePower = new MetricUnit(compound.getDouble("reactivePower"));
        simId = compound.getUniqueId("simId");
    }

    @Override
    public void receiveSimulationResults(JsonObject results) {
        this.resultPower = new MetricUnit(results.get("p_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.reactivePower = new MetricUnit(results.get("q_mvar").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        notifyUpdate();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonObject bus = getBusJson(busVoltage);
        UUID busId = embededBusses.get("main");

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("in_service", inService);
        obj.addProperty("bus", busId.toString());
        obj.addProperty("vm_pu", slackVoltage.get());


        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void zeroSim() {
        resultPower = new MetricUnit(0);
        reactivePower = new MetricUnit(0);
        notifyUpdate();
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("main", UUID.randomUUID());
    }

    @Override
    public void fillPacketBuffer(double[] d) {
        d[0] = slackVoltage.get();
        d[1] = busVoltage.get();
    }

    @Override
    public int getNumInputs() {
        return 2;
    }

    public MetricUnit getSlackVoltage() {
        return this.slackVoltage;
    }

    public void setSlackVoltage(MetricUnit slackVoltage) {
        this.slackVoltage = slackVoltage;
    }

    public MetricUnit getBusVoltage() {
        return this.busVoltage;
    }

    public void setBusVoltage(MetricUnit busVoltage) {
        this.busVoltage = busVoltage;
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

    @Override
    public void updateOrToggle(PlayerEntity player) {
        inService = !inService;
        requestSimulation(player);
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientUtils.openExternalGridScreen(this, player));
    }

    @Override
    public void disable() {
        inService = false;
        slackVoltage = new MetricUnit(0);
    }
}
