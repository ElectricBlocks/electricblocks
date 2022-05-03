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
        //defaultInputs.put("vn_kv", new SimulationProperty("Bus Voltage", "kV", 20.0, false, 1));
        defaultInputs.put("p_mw", new SimulationProperty("Active Power (+ -)", "MW", 20.0, 1));
        defaultInputs.put("max_e_mwh", new SimulationProperty("Maximum Energy", "MW", 60.0, 1));
        defaultInputs.put("soc_percent", new SimulationProperty("Charge Percent", "%", 5.0, 1));
        defaultInputs.put("vn_kv", new SimulationProperty("Bus Voltage", "kV", 20.0, false, 3));

//        defaultOutputs.put("vm_pu", new SimulationProperty("Voltage Magnitude", "pu", 0.0, 3));
//        defaultOutputs.put("va_degree", new SimulationProperty("Voltage Angle", "degrees", 0.0, 3));
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
        JsonObject bus = getBusJson(inputs.get("vn_kv").getDouble());
        UUID busId = embededBusses.get("main");

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("bus", busId.toString());
        fillJSON(obj);

        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("main", UUID.randomUUID());
    }

    @Override
    public String getTranslationString() {
        return "gui.electricblocks.batteryscreen";
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