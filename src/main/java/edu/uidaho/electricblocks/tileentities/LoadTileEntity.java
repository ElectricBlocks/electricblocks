package edu.uidaho.electricblocks.tileentities;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.IMultimeter;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraftforge.common.util.Constants;

/**
 * Tile entity associated with the @LoadBlock
 */
public class LoadTileEntity extends SimulationTileEntity implements IMultimeter {

    private boolean inService = false;
    private Watt maxPower = new Watt(100);
    private Watt resultPower = new Watt(0);

    public LoadTileEntity() {
        super(RegistryHandler.LOAD_TILE_ENTITY.get(), SimulationType.LOAD);
    }

    /**
     * Adds Load specific information to the NBT Tags
     * @param compound The NBT tag being updated
     * @return A complete NBT tag with Load specific information
     */
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("maxPower", maxPower.getWatts());
        compound.putDouble("resultPower", resultPower.getWatts());
        compound.putUniqueId("simId", simId);
        return compound;
    }

    /**
     * Extracts information from an NBT Tag about the Load
     * @param compound The NBT Tag to extract info from
     */
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        maxPower = new Watt(compound.getDouble("maxPower"));
        resultPower = new Watt(compound.getDouble("resultPower"));
        simId = compound.getUniqueId("simId");
    }

    /**
     * Sever sends an update tile entity packet to client.
     * @return The completed update tile entity packet
     */
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return new SUpdateTileEntityPacket(getPos(), -1, tag);
    }

    /**
     * Data packet received from server regarding Load Tile Entity
     * @param net The network manager
     * @param pkt The update packet
     */
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        read(tag);
    }

    /**
     * Turns the lamp on and off
     */
    public void toggleInService(PlayerEntity player) {
        inService = !inService;
        requestSimulation(player);
    }

    public boolean isInService() {
        return inService;
    }

    /**
     * Return the max power
     * @return
     */
    public Watt getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Watt maxPower) {
        this.maxPower = maxPower;
    }

    public Watt getResultPower() {
        return resultPower;
    }

    public void setResultPower(Watt resultPower) {
        this.resultPower = resultPower;
        
    }

    @Override
    public void receiveSimulationResults(JsonObject results) {
        double resultPower = results.get("p_mw").getAsDouble() * 1000000;
        setResultPower(new Watt(resultPower));
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
        obj.addProperty("p_mw", maxPower.getMegaWatts());
        obj.addProperty("bus", busId.toString());

        json.add(busId.toString(), bus);
        json.add(getSimulationID().toString(), obj);
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

    @Override
    public void initEmbeddedBusses() {
        embededBusses.put("main", UUID.randomUUID());
    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        toggleInService(player);
    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        PlayerUtils.error(player, "command.electricblocks.viewmodify.err_invalid_block");
    }
    
}
