package edu.uidaho.electricblocks.tileentities;

import java.util.UUID;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.guis.LoadScreen;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

/**
 * Tile entity associated with the @LoadBlock
 */
public class LoadTileEntity extends SimulationTileEntity implements IMultimeter {

    private boolean inService = false;
    private MetricUnit maxPower = new MetricUnit(100);
    private MetricUnit resultPower = new MetricUnit(0);
    private MetricUnit reactivePower = new MetricUnit(0);

    public LoadTileEntity() {
        super(RegistryHandler.LOAD_TILE_ENTITY.get(), SimulationType.LOAD);
    }

    /**
     * Adds Load specific information to the NBT Tags
     * @param compound The NBT tag being updated
     * @return A complete NBT tag with Load specific information
     */
    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("maxPower", maxPower.get());
        compound.putDouble("resultPower", resultPower.get());
        compound.putDouble("reactivePower", reactivePower.get());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    /**
     * Extracts information from an NBT Tag about the Load
     * @param compound The NBT Tag to extract info from
     */
    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        maxPower = new MetricUnit(compound.getDouble("maxPower"));
        resultPower = new MetricUnit(compound.getDouble("resultPower"));
        reactivePower = new MetricUnit(compound.getDouble("reactivePower"));
        simId = compound.getUniqueId("simId");
    }

    /**
     * Turns the lamp on and off
     */
    public void toggleInService(PlayerEntity player) {
        inService = !inService;
        requestSimulation(player);
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    public boolean isInService() {
        return inService;
    }

    /**
     * Return the max power
     * @return The max power of this load
     */
    public MetricUnit getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(MetricUnit maxPower) {
        this.maxPower = maxPower;
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
    public void receiveSimulationResults(JsonObject results) {
        setResultPower(new MetricUnit(results.get("p_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA));
        setReactivePower(new MetricUnit(results.get("q_mvar").getAsDouble(), MetricUnit.MetricPrefix.MEGA));
        notifyUpdate();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonObject bus = new JsonObject();
        UUID busId = embededBusses.get("main");
        bus.addProperty("etype", SimulationType.BUS.toString());

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("in_service", inService);
        obj.addProperty("p_mw", maxPower.getMega());
        obj.addProperty("bus", busId.toString());

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
    public void updateOrToggle(PlayerEntity player) {
        toggleInService(player);
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        Minecraft.getInstance().displayGuiScreen(new LoadScreen(this, player));
    }

    @Override
    public void disable() {
        inService = false;
        maxPower = new MetricUnit(0);
    }
    
}
