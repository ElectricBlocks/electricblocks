package edu.uidaho.electricblocks.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.utils.MetricUnit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class SimulationTileEntity extends TileEntity {

    protected boolean inService = false;
    protected UUID simId = UUID.randomUUID();
    protected final SimulationType simulationType;
    protected Map<String, UUID> embededBusses = new HashMap<>();

    public SimulationTileEntity(TileEntityType<?> tileEntityTypeIn, SimulationType simulationType) {
        super(tileEntityTypeIn);
        this.simulationType = simulationType;
        initEmbeddedBusses();
    }

    /**
     * This function is called whenever a simulation involving this simulation tile entity is finished and the results
     * are received.
     * @param jsonObject The results of the simulation for this specific tile entity
     */
    public abstract void receiveSimulationResults(JsonObject jsonObject);

    /**
     * This function is called whenever a simulation involving this tile entity fails for any reason. This sets all
     * the results to zero. This does not zero out any of the inputs, only the results
     */
    public abstract void zeroSim();

    /**
     * This function fully disables the tile entity. This is usually called before a tile entity is destroyed so that
     * the simulation can be updated with this block gone. This usually just involves setting all values to zero
     * and setting the block to no longer be in service.
     */
    public abstract void disable();

    /**
     * Called by SimulationNetwork to get the JSON representation of this tile entity.
     * @return The JSON representation of this object
     */
    public abstract JsonObject toJson();

    /**
     * Initializes the embedded buses. For most blocks this just involves mapping "main" to a new randomly generated
     * UUID, but some blocks will require more than one like the transformer.
     */
    public abstract void initEmbeddedBusses();

    /**
     * The UUID of this simulation entity. Used for tracking the name of this tile entity when it is sent over to the
     * EBPP simulation software.
     * @return The UUID
     */
    public UUID getSimulationID() {
        return simId;
    }

    /**
     * The simulation type enum. This is useful since there may be multiple blocks that are represented in the
     * simulator by a single type. For example, a generic load and lamp are both loads in the simulator.
     * @return The simulation type enum
     */
    public SimulationType getSimulationType() {
        return this.simulationType;
    }

    /**
     * Requests a simulation on this tile entity and its associated network of connected components.
     * This version is called when the player that requested the simulation is unknown.
     */
    public void requestSimulation() {
        requestSimulation(null);
    }

    /**
     * Request a simulation on this tile entity and its associated network of connected components.
     * This version is called when the player that requested the simulation is known. They will receive any errors or
     * other messages associated with this simulation request.
     * @param player The player that requested the simulation if they exist
     */
    public void requestSimulation(@Nullable PlayerEntity player) {
        if (world != null && !world.isRemote()) {
            SimulationHandler.instance().newSimulationNetwork(this, player);
        }
    }

    /**
     * Get's the map of embedded buses
     * @return The map of embedded buses
     */
    public Map<String, UUID> getEmbeddedBuses() {
        return embededBusses;
    }

    /**
     * Called whenever an update needs to be sent between the server and the player. This constructs the packet using
     * information written in the write function for this class.
     * @return The tile entity's update packet
     */
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return new SUpdateTileEntityPacket(getPos(), -1, tag);
    }

    /**
     * Called whenever a data packet is received concerning this tile entity.
     * @param net The network manager
     * @param pkt The update packet to be read from
     */
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        read(tag);
    }

    /**
     * Get the embedded bus that a wire placed at a specific location should be
     * connected to.
     * 
     * May return null if no embedded bus should be located there.
     * Should be overriden by blocks that have more complex behavior such as
     * having multiple buses and orientation specific behaviors.
     * @param pos The position of the wire block that is connected to the embedded bus
     * @return The UUID of the embedded bus assigned to the block at pos
     */
    public UUID getEmbeddedBus(BlockPos pos) {
        if (getPos().manhattanDistance(pos) == 1) {
            return embededBusses.get("main");
        }
        return null;
    }

    /**
     * Called to send update from server to client of changes made involving this tile entity. This function write's the
     * NBT tag, marks it as dirty, and then runs the notifyBlockUpdate function.
     */
    public void notifyUpdate() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        markDirty();
        if (world != null) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    public JsonObject getBusJson() {
        return getBusJson(new MetricUnit(20, MetricUnit.MetricPrefix.KILO));
    }

    public JsonObject getBusJson(MetricUnit ratedVoltageKV) {
        JsonObject bus = new JsonObject();
        bus.addProperty("etype", SimulationType.BUS.toString());
        bus.addProperty("vn_kv", ratedVoltageKV.getKilo());
        return bus;
    }

    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    public abstract void fillPacketBuffer(double[] d);

    /**
     * Gets the number of numerical inputs that the SimulationTileEntity requires for simulation.
     * @return The number of numerical inputs that the SimulationTileEntity requires for simulation.
     */
    public abstract int getNumInputs();
    
}
