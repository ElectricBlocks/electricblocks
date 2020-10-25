package edu.uidaho.electricblocks.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SimulationNetwork {

    private List<SimulationTileEntity> simTileEntities = new ArrayList<>();
    private boolean ready = false;
    private Thread asyncBlocksThread;

    public SimulationNetwork(SimulationTileEntity startingBlock) {
        simTileEntities.add(startingBlock);
        asyncBlocksThread = new Thread() {
            public void run() {
                addConnectedBlocks();
                setReady();
            }
        };
        asyncBlocksThread.start();
    }

    public List<SimulationTileEntity> getSimulationList() {
        return simTileEntities;
    }

    public void handleSimulationResults(JsonObject simResults) {
        JsonObject elements = simResults.get("elements").getAsJsonObject();
        for (SimulationTileEntity sim : simTileEntities) {
            sim.receiveSimulationResults(elements.get(sim.getSimulationID().toString()).getAsJsonObject());
        }
    }

    public void zeroSimResults() {
        for (SimulationTileEntity sim : simTileEntities) {
            sim.zeroSim();
        }
    }

    private void setReady() {
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public void addConnectedBlocks() {
        HashSet<BlockPos> checked = new HashSet<>();
        Queue<BlockPos> unchecked = new LinkedList<>();
        World world = simTileEntities.get(0).getWorld();
        unchecked.add(simTileEntities.get(0).getPos());
        
        while (!unchecked.isEmpty()) {
            BlockPos bPos = unchecked.remove();
            if (checked.contains(bPos)) continue; // Skip if this block was already checked

            TileEntity te = world.getChunk(bPos).getTileEntity(bPos); // Might not be thread safe
            if (te != null && te instanceof SimulationTileEntity) {
                SimulationTileEntity ste = (SimulationTileEntity) te;
                simTileEntities.add(ste);
                unchecked.add(bPos.up());
                unchecked.add(bPos.down());
                unchecked.add(bPos.north());
                unchecked.add(bPos.south());
                unchecked.add(bPos.east());
                unchecked.add(bPos.west());
            }
            checked.add(bPos);
        }
    }

}
