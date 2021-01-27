package edu.uidaho.electricblocks.tileentities;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Volt;
import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;

public class GeneratorTileEntity extends SimulationTileEntity {

    private boolean inService = false;
    private boolean slack = false;
    private Watt maxPower = new Watt(1000);
    private Watt resultPower = new Watt(0);
    private Watt reactivePower = new Watt(0);
    private Volt nominalVoltage = new Volt(120);
    private Volt resultVoltage = new Volt(0);

    public GeneratorTileEntity() {
        super(RegistryHandler.GENERATOR_TILE_ENTITY.get(), SimulationType.GENERATOR);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putBoolean("slack", slack);
        compound.putDouble("maxPower", maxPower.getWatts());
        compound.putDouble("resultPower", resultPower.getWatts());
        compound.putDouble("reactivePower", reactivePower.getWatts());
        compound.putDouble("nominalVoltage", nominalVoltage.getVolts());
        compound.putDouble("resultVoltage", resultVoltage.getVolts());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        slack = compound.getBoolean("slack");
        maxPower = new Watt(compound.getDouble("maxPower"));
        resultPower = new Watt(compound.getDouble("resultPower"));
        reactivePower = new Watt(compound.getDouble("reactivePower"));
        nominalVoltage = new Volt(compound.getDouble("nominalVoltage"));
        resultVoltage = new Volt(compound.getDouble("resultVoltage"));
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
        double resultVoltage = results.get("vm_pu").getAsDouble();
        this.resultVoltage = new Volt(resultVoltage);
        notifyUpdate();
    }

    @Override
    public void zeroSim() {
        resultPower = new Watt(0);
        reactivePower = new Watt(0);
        resultVoltage = new Volt(0);
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
        obj.addProperty("slack", slack);
        obj.addProperty("in_service", inService);
        obj.addProperty("bus", busId.toString());
        obj.addProperty("p_mw", maxPower.getMegaWatts());
        obj.addProperty("vm_pu", nominalVoltage.getVolts());
        
        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
        return json;
    }

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("main", UUID.randomUUID());
    }

    
}
