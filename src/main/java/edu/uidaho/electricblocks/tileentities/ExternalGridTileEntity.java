package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Volt;
import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.guis.ExternalGridScreen;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;

import java.util.UUID;

import javax.annotation.Nullable;

public class ExternalGridTileEntity extends SimulationTileEntity implements IMultimeter {

    private boolean inService = false;
    private Volt voltage = new Volt(1);
    private Watt resultPower = new Watt(0);
    private Watt reactivePower = new Watt(0);

    public ExternalGridTileEntity() {
        super(RegistryHandler.EXTERNAL_GRID_TILE_ENTITY.get(), SimulationType.EXT_GRID);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("voltage", voltage.getVolts());
        compound.putDouble("resultPower", resultPower.getWatts());
        compound.putDouble("reactivePower", reactivePower.getWatts());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        voltage = new Volt(compound.getDouble("voltage"));
        resultPower = new Watt(compound.getDouble("resultPower"));
        reactivePower = new Watt(compound.getDouble("reactivePower"));
        simId = compound.getUniqueId("simId");
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return new SUpdateTileEntityPacket(getPos(), -1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        read(tag);
    }

    @Override
    public void receiveSimulationResults(JsonObject results) {
        double resultPower = results.get("p_mw").getAsDouble() * 1000000;
        this.resultPower = new Watt(resultPower);
        double reactivePower = results.get("q_mvar").getAsDouble() * 1000000;
        this.reactivePower = new Watt(reactivePower);
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
        obj.addProperty("vm_pu", voltage.getVolts());
        obj.addProperty("bus", busId.toString());

        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void zeroSim() {
        resultPower = new Watt(0);
        reactivePower = new Watt(0);
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
    
    public Volt getVoltage() {
        return this.voltage;
    }

    public void setVoltage(Volt voltage) {
        this.voltage = voltage;
    }

    public Watt getResultPower() {
        return resultPower;
    }

    public void setResultPower(Watt resultPower) {
        this.resultPower = resultPower;
    }

    public Watt getReactivePower() {
        return reactivePower;
    }

    public void setReactivePower(Watt reactivePower) {
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
        voltage = new Volt(0);
    }
}
