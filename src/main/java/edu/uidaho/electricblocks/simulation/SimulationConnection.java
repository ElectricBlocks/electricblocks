package edu.uidaho.electricblocks.simulation;

import java.util.UUID;

import com.google.gson.JsonObject;

/**
 * SimulationConnection represents a line/wire that is ran between two buses in a SimulationNetwork.
 */
public class SimulationConnection {
    private UUID simId;
    private UUID fromBus;
    private UUID toBus;
    private double lengthKm = 0.0;
    private String stdType = "NAYY 4x50 SE";

    public SimulationConnection() {
        this.simId = UUID.randomUUID();
    }

    /**
     * Gets the UUID assigned to this line
     * @return The UUID assigned to this line.
     */
    public UUID getSimId() {
        return simId;
    }

    /**
     * Gets the JSON representation of this line for use in simulations
     * @return The JSON representation of this line
     */
    public JsonObject toJson() throws NullPointerException {
        JsonObject json = new JsonObject();
        json.addProperty("etype", "line");
        json.addProperty("from_bus", fromBus.toString());
        json.addProperty("to_bus", toBus.toString());
        json.addProperty("length_km", lengthKm);
        json.addProperty("std_type", stdType);
        return json;
    }

    /**
     * Increments the length of this line by 1 meter (one block)
     */
    public void incrementLength() {
        this.lengthKm += 0.001;
    }

    /**
     * Gets the UUID assigned to the bus at the start of this line
     * @return The UUID assigned to the bus at the start of this line
     */
    public UUID getFromBus() {
        return this.fromBus;
    }

    /**
     * Sets the UUID assigned to the bus at the start of this line
     * @param fromBus The UUID assigned to the bus at the start of this line
     */
    public void setFromBus(UUID fromBus) {
        this.fromBus = fromBus;
    }

    /**
     * Gets the UUID assigned to the bus at the end of this line
     * @return The UUID assigned to the bus at the end of this line
     */
    public UUID getToBus() {
        return this.toBus;
    }

    /**
     * Sets the UUID assigned to the bus at the end of this line
     * @param toBus The UUID assigned to the bus at the end of this line
     */
    public void setToBus(UUID toBus) {
        this.toBus = toBus;
    }

    /**
     * Gets the length of this line in kilometers
     * @return The length of this line in kilometers
     */
    public double getLengthKm() {
        return this.lengthKm;
    }

}
