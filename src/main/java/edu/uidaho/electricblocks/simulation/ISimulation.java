package edu.uidaho.electricblocks.simulation;

import com.google.gson.JsonObject;

import java.util.UUID;

public interface ISimulation {
    public UUID getSimulationID();
    public SimulationType getSimulationType();
    public void receiveSimulationResults(JsonObject results);
    public void zeroSim();
    public JsonObject toJson();
}
