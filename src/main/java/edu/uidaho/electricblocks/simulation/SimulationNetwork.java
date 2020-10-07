package edu.uidaho.electricblocks.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;

public class SimulationNetwork {

    private List<ISimulation> iSimulations = new ArrayList<>();
    private boolean ready = false;

    public SimulationNetwork(ISimulation startingBlock) {
        iSimulations.add(startingBlock);
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

    public void addConnectedBlocks() {
        // TODO This whole monstrosity
    }

}
