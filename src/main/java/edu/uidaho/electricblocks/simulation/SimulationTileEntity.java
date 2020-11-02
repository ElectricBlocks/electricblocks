package edu.uidaho.electricblocks.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public abstract class SimulationTileEntity extends TileEntity {

    protected UUID simId = UUID.randomUUID();
    protected final SimulationType simulationType;
    protected Map<String, UUID> embededBusses = new HashMap<>();

    public SimulationTileEntity(TileEntityType<?> tileEntityTypeIn, SimulationType simulationType) {
        super(tileEntityTypeIn);
        this.simulationType = simulationType;
        initEmbeddedBusses();
    }

    public abstract void receiveSimulationResults(JsonObject jsonObject);

    public abstract void zeroSim();

    public abstract JsonObject toJson();

    public abstract void initEmbeddedBusses();

    public UUID getSimulationID() {
        return simId;
    }

    public SimulationType getSimulationType() {
        return this.simulationType;
    }

    public void requestSimulation() {
        requestSimulation(null);
    }

    public void requestSimulation(PlayerEntity player) {
        if (!world.isRemote()) {
            SimulationHandler.instance().newSimulationNetwork(this, player);
        }
    }

    public Map<String, UUID> getEmbeddedBuses() {
        return embededBusses;
    }

    /**
     * Get the embedded bus that a wire placed at a specific location should be
     * connected to.
     * 
     * May return null if no embedded bus should be located there.
     * Should be overriden by blocks that have more complex behavior such as
     * having multiple buses and orientation specific behaviors.
     * @param pos
     * @return
     */
    public UUID getEmbeddedBus(BlockPos pos) {
        if (getPos().manhattanDistance(pos) == 1) {
            return embededBusses.get("main");
        }
        return null;
    }

    public void notifyUpdate() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        markDirty();
        if (world != null) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }
    
}
