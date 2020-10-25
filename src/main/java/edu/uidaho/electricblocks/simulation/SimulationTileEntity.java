package edu.uidaho.electricblocks.simulation;

import java.util.UUID;

import com.google.gson.JsonObject;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class SimulationTileEntity extends TileEntity {

    protected UUID simId = UUID.randomUUID();
    protected final SimulationType simulationType;

    public SimulationTileEntity(TileEntityType<?> tileEntityTypeIn, SimulationType simulationType) {
        super(tileEntityTypeIn);
        this.simulationType = simulationType;
    }

    public abstract void receiveSimulationResults(JsonObject jsonObject);

    public abstract void zeroSim();

    public abstract JsonObject toJson();

    public UUID getSimulationID() {
        return simId;
    }

    public SimulationType getSimulationType() {
        return this.simulationType;
    }

    public void requestSimulation() {
        if (!world.isRemote()) {
            SimulationHandler.instance().newSimulationNetwork(this);
        }
    }
    
}
