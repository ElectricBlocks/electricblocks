package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Volt;
import edu.uidaho.electricblocks.simulation.ISimulation;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class ExternalGridTileEntity extends TileEntity implements ISimulation {

    private boolean inService = false;
    private Volt voltageLevel = new Volt(120);
    private UUID simId = UUID.randomUUID();

    public ExternalGridTileEntity() {
        super(RegistryHandler.EXTERNAL_GRID_TILE_ENTITY.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("voltageLevel", voltageLevel.getVolts());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        voltageLevel = new Volt(compound.getDouble("voltageLevel"));
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
    public UUID getSimulationID() {
        return simId;
    }

    @Override
    public SimulationType getSimulationType() {
        return SimulationType.EXT_GRID;
    }

    @Override
    public void addOrUpdateSimulation(JsonObject simulation) {

    }

    @Override
    public void receiveSimulationResults(JsonObject results) {

    }

    @Override
    public JsonObject toJson() {
        // TODO Auto-generated method stub
        return null;
    }
}
