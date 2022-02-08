package edu.uidaho.electricblocks.simulation;

public enum SimulationType {
    GENERATOR("gen"),
    LOAD("load"),
    LINE("line"),
    EXT_GRID("ext_grid"),
    BUS("bus"),
    SWITCH("switch"),
    TRANSFORMER("trafo"),
    BATTERY ("storage"),
    ASYMMETRIC_LOAD("asymmetric_load"),
    ASYMMETRIC_GENERATOR("asymmetric_sgen"),
    ELEC_FURNACE("elec_furnace"),
    THREE_WINDING_TRANSFORMER("trafo3w");

    private final String name;

    SimulationType(String name) {
        this.name = name;
    }

    /**
     * Converts the simulation type represented by an instance of the SimulationType enum into the string representation
     * used by PandaPower in the EBPP simulation server. This is used by tile entities to mark their EBPP type or
     * "etype".
     * @return The EBPP string representing the simulation type
     */
    public String toString() {
        return this.name;
    }
}
