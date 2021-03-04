package edu.uidaho.electricblocks.tileentities;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import edu.uidaho.electricblocks.utils.ClientUtils;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;

/**
 * Tile entity associated with the @LoadBlock
 */
public class LoadTileEntity extends SimulationTileEntity {

    protected static final Map<String, SimulationProperty> defaultInputs = new LinkedHashMap<>();
    protected static final Map<String, SimulationProperty> defaultOutputs = new LinkedHashMap<>();

    static {
        defaultInputs.put("in_service", new SimulationProperty("In Service", "N/a", false));
        defaultInputs.put("p_mw", new SimulationProperty("Active Power", "MW", 1.0));
        defaultInputs.put("q_mvar", new SimulationProperty("Reactive Power", "Mvar", 0.0));
        defaultInputs.put("vn_kv", new SimulationProperty("Bus Voltage", "kV", 20.0, false));

        defaultOutputs.put("p_mw", new SimulationProperty("Active Power", "MW", 0.0));
        defaultOutputs.put("q_mvar", new SimulationProperty("Reactive Power", "Mvar", 0.0));
    }

    public LoadTileEntity() {
        super(RegistryHandler.LOAD_TILE_ENTITY.get(), SimulationType.LOAD);
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
        return "gui.electricblocks.loadscreen";
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