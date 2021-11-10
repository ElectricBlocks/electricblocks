package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class BatteryTileEntity extends SimulationTileEntity implements IMultimeter {

    protected static final Map<String, SimulationProperty > defaultInputs = new LinkedHashMap<>();
    protected static final Map<String, SimulationProperty> defaultOutputs = new LinkedHashMap<>();

    //changes outputs of multimeter will need to be changed to reflect relevant data once fully developed.

    static {
        defaultInputs.put("in_service", new SimulationProperty("In Service", "N/a", false, 1));
        defaultInputs.put("vn_kv", new SimulationProperty("Bus Voltage", "kV", 20.0, 1));

        defaultOutputs.put("vm_pu", new SimulationProperty("Voltage Magnitude", "pu", 0.0, 3));
        defaultOutputs.put("va_degree", new SimulationProperty("Voltage Angle", "degrees", 0.0, 3));
        defaultOutputs.put("p_mw", new SimulationProperty("Active Power", "MW", 0.0, 1));
        defaultOutputs.put("q_mvar", new SimulationProperty("Reactive Power", "Mvar", 0.0, 2));
    }
        //Sim Type changes from bus to battery once developed SO UI is correct.
    public BatteryTileEntity() {
        super(RegistryHandler.BATTERY_TILE_ENTITY.get(), SimulationType.BATTERY);
    }

    @Override
    //changes toJson will need to be changed to reflect relevant data once fully developed.
    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        fillJSON(obj);

        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void initEmbeddedBusses() {
        // No embedded busses!
    }

    // Overriding since there are no embedded buses
    @Override
    public UUID getEmbeddedBus(BlockPos pos) {
        if (getPos().manhattanDistance(pos) == 1) {
            return simId;
        }
        return null;
    }
// not a bus, is set to a bus to make sure integration works.
    @Override
    public String getTranslationString() {
        return "gui.electricblocks.busscreen";
    }

    @Override
    public Map<String, SimulationProperty> getDefaultInputs() {
        return defaultInputs;
    }

    @Override
    public Map<String, SimulationProperty> getDefaultOutputs() {
        return defaultOutputs;
    }
//TO DO - CREATE Simulation Properties for battery blocks.
}