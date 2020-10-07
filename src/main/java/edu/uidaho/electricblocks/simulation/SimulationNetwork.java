package edu.uidaho.electricblocks.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.ElectricBlocksMod;

public class SimulationNetwork {

    private List<ISimulation> iSimulations = new ArrayList<>();
    private boolean ready = false;
    private Thread asyncBlocksThread;

    public SimulationNetwork(ISimulation startingBlock) {
        iSimulations.add(startingBlock);
        asyncBlocksThread = new Thread() {
            public void run() {
                addConnectedBlocks();
                setReady();
            }
        };
        asyncBlocksThread.start();
    }

    public List<ISimulation> getSimulationList() {
        return iSimulations;
    }

    public void handleSimulationResults(JsonObject simResults) {
        JsonObject elements = simResults.get("elements").getAsJsonObject();
        for (ISimulation sim : iSimulations) {
            sim.receiveSimulationResults(elements.get(sim.getSimulationID().toString()).getAsJsonObject());
        }
    }

    public void zeroSimResults() {
        for (ISimulation sim : iSimulations) {
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
