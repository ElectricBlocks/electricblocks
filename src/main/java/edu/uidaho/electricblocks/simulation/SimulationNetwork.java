package edu.uidaho.electricblocks.simulation;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.ElectricBlocksMod;

public class SimulationNetwork {

    private List<SimulationTileEntity> simTileEntities = new ArrayList<>();
    private boolean ready = false;
    private Thread asyncBlocksThread;

    public SimulationNetwork(SimulationTileEntity startingBlock) {
        simTileEntities.add(startingBlock);
        asyncBlocksThread = new Thread() {
            public void run() {
                addConnectedBlocks();
                setReady();
            }
        };
        asyncBlocksThread.start();
    }

    public List<SimulationTileEntity> getSimulationList() {
        return simTileEntities;
    }

    public void handleSimulationResults(JsonObject simResults) {
        JsonObject elements = simResults.get("elements").getAsJsonObject();
        for (SimulationTileEntity sim : simTileEntities) {
            sim.receiveSimulationResults(elements.get(sim.getSimulationID().toString()).getAsJsonObject());
        }
    }

    public void zeroSimResults() {
        for (SimulationTileEntity sim : simTileEntities) {
            sim.zeroSim();
        }
    }

    private void setReady() {
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public void addConnectedBlocks() {

    }

}
