package edu.uidaho.electricblocks.tileentities;

import java.util.UUID;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Volt;
import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.guis.GeneratorScreen;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class GeneratorTileEntity extends SimulationTileEntity implements IMultimeter {

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

    public boolean isInService() {
        return this.inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    public Watt getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Watt maxPower) {
        this.maxPower = maxPower;
    }

    public boolean isSlack() {
        return slack;
    }

    public void setSlack(boolean slack) {
        this.slack = slack;
    }

    public Volt getNominalVoltage() {
        return nominalVoltage;
    }

    public void setNominalVoltage(Volt nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
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

    public Volt getResultVoltage() {
        return resultVoltage;
    }

    public void setResultVoltage(Volt resultVoltage) {
        this.resultVoltage = resultVoltage;
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        inService = !inService;
        notifyUpdate();
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        Minecraft.getInstance().displayGuiScreen(new GeneratorScreen(this, player));
    }

    @Override
    public void disable() {
        inService = false;
        nominalVoltage = new Volt(0);
        maxPower = new Watt(0);
    }
    
}
