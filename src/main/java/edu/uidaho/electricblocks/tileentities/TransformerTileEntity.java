package edu.uidaho.electricblocks.tileentities;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.guis.TransformerScreen;
import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class TransformerTileEntity extends SimulationTileEntity implements IMultimeter {

    public TransformerTileEntity() {
        super(RegistryHandler.TRANSFORMER_TILE_ENTITY.get(), SimulationType.TRANSFORMER);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        // TODO Auto-generated method stub
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        // TODO Auto-generated method stub
        super.read(compound);
    }

    @Override
    public void receiveSimulationResults(JsonObject jsonObject) {
        // TODO Auto-generated method stub

    }

    @Override
    public void zeroSim() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub

    }

    @Override
    public JsonObject toJson() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void initEmbeddedBusses() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateOrToggle(PlayerEntity player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void viewOrModify(PlayerEntity player) {
        Minecraft.getInstance().displayGuiScreen(new TransformerScreen(this, player));
    }
    
}
