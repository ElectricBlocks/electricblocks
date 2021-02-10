package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.utils.MetricUnit;
import edu.uidaho.electricblocks.guis.ExternalGridScreen;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class ExternalGridTileEntity extends SimulationTileEntity implements IMultimeter {

    private boolean inService = false;
    private MetricUnit voltage = new MetricUnit(1);
    private MetricUnit resultPower = new MetricUnit(0);
    private MetricUnit reactivePower = new MetricUnit(0);

    public ExternalGridTileEntity() {
        super(RegistryHandler.EXTERNAL_GRID_TILE_ENTITY.get(), SimulationType.EXT_GRID);
    }


    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("voltage", voltage.get());
        compound.putDouble("resultPower", resultPower.get());
        compound.putDouble("reactivePower", reactivePower.get());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        voltage = new MetricUnit(compound.getDouble("voltage"));
        resultPower = new MetricUnit(compound.getDouble("resultPower"));
        reactivePower = new MetricUnit(compound.getDouble("reactivePower"));
        simId = compound.getUniqueId("simId");
    }

    @Override
    public void receiveSimulationResults(JsonObject results) {
        this.resultPower = new MetricUnit(results.get("p_mw").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        this.reactivePower = new MetricUnit(results.get("q_mvar").getAsDouble(), MetricUnit.MetricPrefix.MEGA);
        notifyUpdate();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonObject bus = new JsonObject();
        UUID busId = embededBusses.get("main");
        bus.addProperty("etype", SimulationType.BUS.toString());

        JsonObject obj = new JsonObject();
        obj.addProperty("etype", getSimulationType().toString());
        obj.addProperty("in_service", inService);
        obj.addProperty("vm_pu", voltage.get());
        obj.addProperty("bus", busId.toString());

        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void zeroSim() {
        resultPower = new MetricUnit(0);
        reactivePower = new MetricUnit(0);
        notifyUpdate();
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("main", UUID.randomUUID());
    }

    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }
    
    public MetricUnit getVoltage() {
        return this.voltage;
    }

    public void setVoltage(MetricUnit voltage) {
        this.voltage = voltage;
    }

    public MetricUnit getResultPower() {
        return resultPower;
    }

    public void setResultPower(MetricUnit resultPower) {
        this.resultPower = resultPower;
    }

    public MetricUnit getReactivePower() {
        return reactivePower;
    }

    public void setReactivePower(MetricUnit reactivePower) {
        this.reactivePower = reactivePower;
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        inService = !inService;
        requestSimulation(player);
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        Minecraft.getInstance().displayGuiScreen(new ExternalGridScreen(this, player));
    }

    @Override
    public void disable() {
        inService = false;
        voltage = new MetricUnit(0);
    }
}
