package edu.uidaho.electricblocks.simulation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that handles simulation tasks.
 * This class contains a list of SimulationNetworks. Each SimulationNetwork is responsible for converting itself into a
 * format that can be understood by the simulator. This class handles the actual communication between the simulator
 * and the SimulationNetworks. When the simulation is complete and results are received, the results are passed off to
 * the SimulationNetwork to be parsed and then to update each element in the Simulation.
 */
public class SimulationHandler {

    private static SimulationHandler instance = null;
    private List<SimulationNetwork> networkList = new ArrayList<>();

    private SimulationHandler () { }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static SimulationHandler instance() {
        if (instance == null) {
            instance = new SimulationHandler();
        }
        return instance;
    }

}
