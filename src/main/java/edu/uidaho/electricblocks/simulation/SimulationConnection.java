package edu.uidaho.electricblocks.simulation;

import java.util.UUID;

import com.google.gson.JsonObject;

public class SimulationConnection {
    private UUID simId;
    private UUID fromBus;
    private UUID toBus;
    private double lengthKm = 0.0;
    private String stdType = "NAYY 4x50 SE";

    public SimulationConnection() {
        this.simId = UUID.randomUUID();
    }

    public UUID getSimId() {
        return simId;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("etype", "line");
        json.addProperty("from_bus", fromBus.toString());
        json.addProperty("to_bus", toBus.toString());
        json.addProperty("length_km", lengthKm);
        json.addProperty("std_type", stdType);
        return json;
    }

    public void incrementLength() {
        this.lengthKm += 0.001;
    }

    public UUID getSimID() {
        return this.simId;
    }

    public UUID getFromBus() {
        return this.fromBus;
    }

    public void setFromBus(UUID fromBus) {
        this.fromBus = fromBus;
    }

    public UUID getToBus() {
        return this.toBus;
    }

    public void setToBus(UUID toBus) {
        this.toBus = toBus;
    }

    public double getLengthKm() {
        return this.lengthKm;
    }

}
