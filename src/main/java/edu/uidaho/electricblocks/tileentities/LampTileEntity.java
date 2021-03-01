package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import edu.uidaho.electricblocks.utils.ClientUtils;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * LampTileEntity stores information about the lamp block.
 */
public class LampTileEntity extends SimulationTileEntity implements IMultimeter {

    static {
        defaultInputs.put("p_mw", new SimulationProperty<>("Active Power", "MW", 100000.0));
        defaultInputs.put("q_mvar", new SimulationProperty<>("Reactive Power", "Mvar", 1000.0));
        defaultInputs.put("vn_kv", new SimulationProperty<>("Bus Voltage", "kV", 20000.0));

        defaultOutputs.put("p_mw", new SimulationProperty<>("Active Power", "MW", 0.0));
        defaultOutputs.put("q_mvar", new SimulationProperty<>("Reactive Power", "Mvar", 0));
    }

    public LampTileEntity() {
        super(RegistryHandler.LAMP_TILE_ENTITY.get(), SimulationType.LOAD);
    }

    /**
     * Extracts information from an NBT Tag about the Lamp
     * @param compound The NBT Tag to extract info from
     */
    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        if (world != null) {
            world.getLightManager().checkBlock(pos);
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonObject bus = getBusJson(new MetricUnit(inputs.get("vn_kv").getDouble()));
        UUID busId = embededBusses.get("main");

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("bus", busId.toString());
        fillJSON();

        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        toggleInService(player);
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientUtils.openLampScreen(this, player));
    }
}
