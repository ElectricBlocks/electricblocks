package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Watt;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

import javax.annotation.Nullable;

/**
 * LampTileEntity stores information about the lamp block.
 */
public class LampTileEntity extends SimulationTileEntity {

    private boolean inService = false; // Whether or not the lamp is on
    private Watt maxPower = new Watt(60); // Maximum power this lamp can take
    private Watt resultPower = new Watt(0); // Amount of power being received

    public LampTileEntity() {
        super(RegistryHandler.LAMP_TILE_ENTITY.get(), SimulationType.LOAD);
    }

    /**
     * Adds Lamp specific information to the NBT Tags
     * @param compound The NBT tag being updated
     * @return A complete NBT tag with Lamp specific information
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
     * Extracts information from an NBT Tag about the Lamp
     * @param compound The NBT Tag to extract info from
     */
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        maxPower = new Watt(compound.getDouble("maxPower"));
        resultPower = new Watt(compound.getDouble("resultPower"));
        simId = compound.getUniqueId("simId");
        world.getLightManager().checkBlock(pos);
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
     * Data packet received from server regarding Lamp Tile Entity
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

    /**
     * This function takes the active power that the lamp and compares it to the required power for the lamp to work.
     * @return a light value from [0-15]
     */
    public int getScaledLightValue() {
        double percentPower = resultPower.getWatts() / maxPower.getWatts();
        return (int) Math.round(percentPower * 15);
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
}
