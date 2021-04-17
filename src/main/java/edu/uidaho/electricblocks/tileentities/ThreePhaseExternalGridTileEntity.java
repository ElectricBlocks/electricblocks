package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ThreePhaseExternalGridTileEntity extends SimulationTileEntity {

    protected static final Map<String, SimulationProperty> defaultInputs = new LinkedHashMap<>();
    protected static final Map<String, SimulationProperty> defaultOutputs = new LinkedHashMap<>();

    static {
        defaultInputs.put("in_service", new SimulationProperty("In Service", "N/a", false));
        defaultInputs.put("vm_pu", new SimulationProperty("Slack Voltage", "pu", 1.0));
        defaultInputs.put("va_degree", new SimulationProperty("Voltage Angle", "degrees", 0.0));
        defaultInputs.put("vn_kv", new SimulationProperty("Bus Voltage", "kV", 20.0, false));
        defaultInputs.put("s_sc_max_mva", new SimulationProperty("Max S/C Power", "MVA", 1000.0));
        defaultInputs.put("s_sc_min_mva", new SimulationProperty("Min S/C Power", "MVA", 0.0));
        defaultInputs.put("rx_max", new SimulationProperty("Max R/X S/C", "impd ratio", 0.1));
        defaultInputs.put("rx_min", new SimulationProperty("Min R/X S/C", "impd ratio", 0.0));
        defaultInputs.put("r0x0_max", new SimulationProperty("Max 0 Seq R/X", "internal impd ratio", 0.1));
        defaultInputs.put("x0x_max", new SimulationProperty("Max 0 Seq X0/X", "internal impd ratio", 1.0));

        defaultOutputs.put("p_a_mw", new SimulationProperty("Phase A Power", "MW", 0.0));
        defaultOutputs.put("q_a_mvar", new SimulationProperty("A Reactive Power", "Mvar", 0.0));
        defaultOutputs.put("p_b_mw", new SimulationProperty("Phase B Power", "MW", 0.0));
        defaultOutputs.put("q_b_mvar", new SimulationProperty("B Reactive Power", "Mvar", 0.0));
        defaultOutputs.put("p_c_mw", new SimulationProperty("Phase C Power", "MW", 0.0));
        defaultOutputs.put("q_c_mvar", new SimulationProperty("C Reactive Power", "Mvar", 0.0));
    }

    public ThreePhaseExternalGridTileEntity() {
        super(RegistryHandler.THREE_PHASE_EXTERNAL_GRID_TILE_ENTITY.get(), SimulationType.EXT_GRID);
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
        return "gui.electricblocks.threephaseexternalgridscreen";
    }

    @Override
    public Map<String, SimulationProperty> getDefaultInputs() {
        return defaultInputs;
    }

    @Override
    public Map<String, SimulationProperty> getDefaultOutputs() {
        return defaultOutputs;
    }

    @Override
    public boolean isThreePhase() {
        return true;
    }
}