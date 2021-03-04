package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.blocks.TransformerBlock;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class TransformerTileEntity extends SimulationTileEntity {

    protected static final Map<String, SimulationProperty> defaultInputs = new LinkedHashMap<>();
    protected static final Map<String, SimulationProperty> defaultOutputs = new LinkedHashMap<>();

    static {
        defaultInputs.put("in_service", new SimulationProperty("In Service", "N/a", false));
        defaultInputs.put("sn_mva", new SimulationProperty("Apparent Power", "MVA", 40.0));
        defaultInputs.put("vn_hv_kv", new SimulationProperty("High Bus Voltage", "kV", 110.0));
        defaultInputs.put("vn_lv_kv", new SimulationProperty("Low Bus Voltage", "kV", 10.0));
        defaultInputs.put("vkr_percent", new SimulationProperty("S/C Voltage Real", "%", 0.3));
        defaultInputs.put("vk_percent", new SimulationProperty("S/C Voltage", "%", 10.0));
        defaultInputs.put("pfe_kw", new SimulationProperty("Iron Losses", "kW", 30.0));
        defaultInputs.put("i0_percent", new SimulationProperty("Open Loop Losses", "%", 0.1));
        defaultInputs.put("shift_degree", new SimulationProperty("Shift Degree", "degrees", 30.0));

        defaultOutputs.put("p_hv_mw", new SimulationProperty("HV Power", "MW", 0.0));
        defaultOutputs.put("q_hv_mvar", new SimulationProperty("HV Reactive Power", "Mvar", 0.0));
        defaultOutputs.put("p_lv_mw", new SimulationProperty("LV Power", "MW", 0.0));
        defaultOutputs.put("q_lv_mvar", new SimulationProperty("LV Reactive Power", "Mvar", 0.0));
        defaultOutputs.put("pl_mw", new SimulationProperty("Power Losses", "MW", 0.0));
        defaultOutputs.put("ql_mvar", new SimulationProperty("R Power Losses", "Mvar", 0.0));
        defaultOutputs.put("i_hv_ka", new SimulationProperty("HV Bus Current", "kA", 0.0));
        defaultOutputs.put("i_lv_ka", new SimulationProperty("LV Bus Current", "kA", 0.0));
        defaultOutputs.put("vm_hv_pu", new SimulationProperty("HV Voltage MAG", "pu", 0.0));
        defaultOutputs.put("vm_lv_pu", new SimulationProperty("LV Voltage MAG", "pu", 0.0));
        defaultOutputs.put("loading_percent", new SimulationProperty("Loading Percent", "%", 0.0));
    }

    private Direction cachedDirection = Direction.NORTH; // Cache direction for when block is broken

    public TransformerTileEntity() {
        super(RegistryHandler.TRANSFORMER_TILE_ENTITY.get(), SimulationType.TRANSFORMER);
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        JsonObject lvBus = getBusJson(inputs.get("vn_lv_kv").getDouble());
        UUID lvBusId = embededBusses.get("lowVoltage");

        JsonObject hvBus = getBusJson(inputs.get("vn_hv_kv").getDouble());
        UUID hvBusId = embededBusses.get("highVoltage");

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("hv_bus", hvBusId.toString());
        obj.addProperty("lv_bus", lvBusId.toString());
        fillJSON(obj);

        json.add(lvBusId.toString(), lvBus);
        json.add(hvBusId.toString(), hvBus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("lowVoltage", UUID.randomUUID());
        embededBusses.put("highVoltage", UUID.randomUUID());
    }

    @Override
    public UUID getEmbeddedBus(BlockPos pos) {
        if (getPos().manhattanDistance(pos) == 1) {
            Direction d;
            try {
                d = getBlockState().get(TransformerBlock.FACING);
                cachedDirection = d;
            } catch (IllegalArgumentException e) {
                d = cachedDirection;
            }

            if (getPos().offset(d).equals(pos)) {
                return embededBusses.get("highVoltage");
            }
            if (getPos().offset(d.getOpposite()).equals(pos)) {
                return embededBusses.get("lowVoltage");
            }
        }
        return null;
    }

    @Override
    public String getTranslationString() {
        return "gui.electricblocks.transformerscreen";
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