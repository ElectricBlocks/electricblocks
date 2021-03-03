package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import edu.uidaho.electricblocks.utils.ClientUtils;
import edu.uidaho.electricblocks.utils.MetricUnit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class BusTileEntity extends SimulationTileEntity implements IMultimeter {

    protected static final Map<String, SimulationProperty> defaultInputs = new LinkedHashMap<>();
    protected static final Map<String, SimulationProperty> defaultOutputs = new LinkedHashMap<>();

    static {
        defaultInputs.put("in_service", new SimulationProperty("In Service", "N/a", false));
        defaultInputs.put("vn_kv", new SimulationProperty("Bus Voltage", "kV", 20.0));

        defaultOutputs.put("vm_pu", new SimulationProperty("Voltage Magnitude", "pu", 0.0));
        defaultOutputs.put("va_degree", new SimulationProperty("Voltage Angle", "degrees", 0.0));
        defaultOutputs.put("p_mw", new SimulationProperty("Active Power", "MW", 0.0));
        defaultOutputs.put("q_mvar", new SimulationProperty("Reactive Power", "Mvar", 0.0));
    }

    public BusTileEntity() {
        super(RegistryHandler.BUS_TILE_ENTITY.get(), SimulationType.BUS);
    }

    @Override
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

}