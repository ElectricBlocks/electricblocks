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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * LampTileEntity stores information about the lamp block.
 */
public class LampTileEntity extends SimulationTileEntity implements IMultimeter {

    private MetricUnit maxPower = new MetricUnit(60); // Maximum power this lamp can take
    private MetricUnit busVoltage = new MetricUnit(20, MetricUnit.MetricPrefix.KILO);
    private MetricUnit resultPower = new MetricUnit(0); // Amount of power being received
    private MetricUnit reactivePower = new MetricUnit(0);

    public LampTileEntity() {
        super(RegistryHandler.LAMP_TILE_ENTITY.get(), SimulationType.LOAD);
    }

    /**
     * Adds Lamp specific information to the NBT Tags
     * @param compound The NBT tag being updated
     * @return A complete NBT tag with Lamp specific information
     */
    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("maxPower", maxPower.get());
        compound.putDouble("busVoltage", busVoltage.get());
        compound.putDouble("resultPower", resultPower.get());
        compound.putDouble("reactivePower", reactivePower.get());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    /**
     * Extracts information from an NBT Tag about the Lamp
     * @param compound The NBT Tag to extract info from
     */
    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        maxPower = new MetricUnit(compound.getDouble("maxPower"));
        busVoltage = new MetricUnit(compound.getDouble("busVoltage"));
        resultPower = new MetricUnit(compound.getDouble("resultPower"));
        reactivePower = new MetricUnit(compound.getDouble("reactivePower"));
        simId = compound.getUniqueId("simId");
        if (world != null) {
            world.getLightManager().checkBlock(pos);
        }
    }

    /**
     * Turns the lamp on and off
     */
    public void toggleInService(PlayerEntity player) {
        inService = !inService;
        requestSimulation(player);
    }

    /**
     * This function takes the active power that the lamp and compares it to the required power for the lamp to work.
     * @return a light value from [0-15]
     */
    public int getScaledLightValue() {
        double percentPower = resultPower.get() / maxPower.get();
        return (int) Math.round(percentPower * 15);
    }

    public double getLightPercentage() {
        return this.resultPower.get() / this.maxPower.get() * 100;
    }
    public MetricUnit getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(MetricUnit maxPower) {
        this.maxPower = maxPower;
    }

    public MetricUnit getBusVoltage() {
        return busVoltage;
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

    @Override
    public void receiveSimulationResults(JsonObject results) {
        setResultPower(new MetricUnit(results.get("p_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA));
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
        obj.addProperty("p_mw", maxPower.getMega());


        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void zeroSim() {
        resultPower = new MetricUnit(0);
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        markDirty();
        if (world != null) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("main", UUID.randomUUID());
    }

    @Override
    public void fillPacketBuffer(double[] d) {
        d[0] = maxPower.get();
        d[1] = busVoltage.get();
    }

    @Override
    public int getNumInputs() {
        return 2;
    }

    @Override
    public void disable() {
        inService = false;
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        toggleInService(player);
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientUtils.openLampScreen(this, player));
    }
}
