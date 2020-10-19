package edu.uidaho.electricblocks.simulation;

public enum SimulationType {
    GENERATOR("gen"),
    LOAD("load"),
    LINE("line"),
    EXT_GRID("ext_grid"),
    BUS("bus"),
    SWITCH("switch"),
    TRANSFORMER("trafo"),
    ASYMMETRIC_LOAD("asymmetric_load"),
    ASYMMETRIC_GENERATOR("asymmetric_sgen"),
    STORAGE("storage"),
    THREE_WINDING_TRANSFORMER("trafo3w");

    private final String name;

    private SimulationType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
