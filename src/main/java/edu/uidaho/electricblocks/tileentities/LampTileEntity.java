package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.simulation.ISimulation;
import edu.uidaho.electricblocks.simulation.SimulationHandler;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * LampTileEntity stores information about the lamp block.
 */
public class LampTileEntity extends TileEntity implements ISimulation {

    private boolean inService = false; // Whether or not the lamp is on
    private Watt maxPower = new Watt(60); // Maximum power this lamp can take
    private Watt resultPower = new Watt(0); // Amount of power being received
    private UUID simId = UUID.randomUUID();

    public LampTileEntity() {
        super(RegistryHandler.LAMP_TILE_ENTITY.get());
    }

    /**
     * Adds Lamp specific information to the NBT Tags
     * @param compound The NBT tag being updated
     * @return A complete NBT tag with Lamp specific information
     */
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("maxPower", maxPower.getWatts());
        compound.putDouble("resultPower", resultPower.getWatts());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    /**
     * Extracts information from an NBT Tag about the Lamp
     * @param compound The NBT Tag to extract info from
     */
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        maxPower = new Watt(compound.getDouble("maxPower"));
        resultPower = new Watt(compound.getDouble("resultPower"));
        simId = compound.getUniqueId("simId");
    }

    /**
     * Sever sends an update tile entity packet to client.
     * @return The completed update tile entity packet
     */
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return new SUpdateTileEntityPacket(getPos(), -1, tag);
    }

    /**
     * Data packet received from server regarding Lamp Tile Entity
     * @param net The network manager
     * @param pkt The update packet
     */
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        read(tag);
    }

    /**
     * Turns the lamp on and off
     */
    public void toggleInService() {
        inService = !inService;
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        markDirty();
        if (!world.isRemote) {
            SimulationHandler.instance().newSimulationNetwork(this);
        }
        if (world != null) { // Only check block if world is loaded
            world.getLightManager().checkBlock(pos);
        }
    }

    /**
     * This function takes the active power that the lamp and compares it to the required power for the lamp to work.
     * @return a light value from [0-15]
     */
    public int getScaledLightValue() {
        double percentPower = resultPower.getWatts() / maxPower.getWatts();
        return (int) Math.round(percentPower * 15);
    }

    public boolean isInService() {
        return inService;
    }

    /**
     * Return the max power
     * @return
     */
    public Watt getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Watt maxPower) {
        this.maxPower = maxPower;
        if (world != null) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    public Watt getResultPower() {
        return resultPower;
    }

    public void setResultPower(Watt resultPower) {
        this.resultPower = resultPower;
        if (world != null) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public UUID getSimulationID() {
        return simId;
    }

    @Override
    public SimulationType getSimulationType() {
        return SimulationType.LOAD;
    }

    @Override
    public void receiveSimulationResults(JsonObject results) {

    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("etype", getSimulationType().toString());
        json.addProperty("in_service", inService);
        json.addProperty("p_mw", maxPower.getMegaWatts());
        return json;
    }

    @Override
    public void zeroSim() {
        resultPower = new Watt(0);
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }
}
