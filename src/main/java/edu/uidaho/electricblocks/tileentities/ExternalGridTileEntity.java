package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Volt;
import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.simulation.ISimulation;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;

public class ExternalGridTileEntity extends TileEntity implements ISimulation {

    private boolean inService = false;
    private Watt maxPower = new Watt(10000);
    private Watt resultPower = new Watt(0);
    private Volt voltageLevel = new Volt(120);
    private UUID simId = UUID.randomUUID();

    public ExternalGridTileEntity() {
        super(RegistryHandler.EXTERNAL_GRID_TILE_ENTITY.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("maxPower", maxPower.getWatts());
        compound.putDouble("resultPower", resultPower.getWatts());
        compound.putDouble("voltageLevel", voltageLevel.getVolts());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        maxPower = new Watt(compound.getDouble("maxPower"));
        resultPower = new Watt(compound.getDouble("resultPower"));
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
    public void receiveSimulationResults(JsonObject results) {

    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonObject props = new JsonObject();
        props.addProperty("type", getSimulationType().toString());
        props.addProperty("in_service", inService);
        props.addProperty("vm_pu", voltageLevel.getVolts());
        json.add(simId.toString(), props);
        return json;
    }

    @Override
    public void zeroSim() {
        resultPower = new Watt(0);
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }
}
