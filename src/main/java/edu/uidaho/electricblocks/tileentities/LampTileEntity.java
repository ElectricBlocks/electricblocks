package edu.uidaho.electricblocks.tileentities;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.electric.Watt;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class LampTileEntity extends TileEntity {

    private boolean inService = true;
    private Watt maxPower = new Watt(60); // Maximum power this lamp can take
    private Watt resultPower = new Watt(60); // Amount of power being received

    public LampTileEntity() {
        super(RegistryHandler.LAMP_TILE_ENTITY.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("inService", inService);
        compound.putDouble("maxPower", maxPower.getWatts());
        compound.putDouble("resultPower", resultPower.getWatts());
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inService = compound.getBoolean("inService");
        maxPower.setWatts(compound.getDouble("maxPower"));
        resultPower.setWatts(compound.getDouble("resultPower"));
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

    public boolean isInService() {
        return inService;
    }

    public void toggleInService() {
        inService = !inService;
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        markDirty();
        world.getLightManager().checkBlock(pos);
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }

    /**
     * This function takes the active power that the lamp and compares it to the required power for the lamp to work.
     * @return a light value from [0-15]
     */
    public int getScaledLightValue() {
        double percentPower = resultPower.getWatts() / maxPower.getWatts();
        return (int) Math.round(percentPower * 15);
    }

}
