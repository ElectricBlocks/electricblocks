package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ExternalGridTileEntity extends SimulationTileEntity {

    protected static final Map<String, SimulationProperty> defaultInputs = new LinkedHashMap<>();
    protected static final Map<String, SimulationProperty> defaultOutputs = new LinkedHashMap<>();

    static {
        defaultInputs.put("in_service", new SimulationProperty("In Service", "N/a", false, 2));
        defaultInputs.put("vm_pu", new SimulationProperty("Slack Voltage", "pu", 1.0, 3));
        defaultInputs.put("va_degree", new SimulationProperty("Voltage Angle", "degrees", 0.0, 3));
        defaultInputs.put("vn_kv", new SimulationProperty("Bus Voltage", "kV", 20.0, false, 3));

        defaultOutputs.put("p_mw", new SimulationProperty("Active Power", "MW", 0.0, 1));
        defaultOutputs.put("q_mvar", new SimulationProperty("Reactive Power", "Mvar", 0.0, 2));
    }

    public ExternalGridTileEntity() {
        super(RegistryHandler.EXTERNAL_GRID_TILE_ENTITY.get(), SimulationType.EXT_GRID);
    }

    @Override
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
        return "gui.electricblocks.externalgridscreen";
    }

    @Override
    public Map<String, SimulationProperty> getDefaultInputs() {
        return defaultInputs;
    }

    @Override
    public Map<String, SimulationProperty> getDefaultOutputs() {
        return defaultOutputs;
    }

}