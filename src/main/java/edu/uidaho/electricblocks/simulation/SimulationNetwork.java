package edu.uidaho.electricblocks.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.JsonObject;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class SimulationNetwork {

    private List<SimulationTileEntity> simTileEntities = new ArrayList<>();
    private List<SimulationConnection> simConnections = new ArrayList<>();
    private boolean ready = false;
    private Thread asyncBlocksThread;

    private SimulationTileEntity startingBlock;
    private World world;
    private PlayerEntity player = null;

    public SimulationNetwork(SimulationTileEntity startingBlock) {
        this.startingBlock = startingBlock;
        this.world = startingBlock.getWorld();
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

    public List<SimulationConnection> getSimulationConnections() {
        return simConnections;
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

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public boolean hasPlayer() {
        return this.player != null;
    }

    public boolean isElectricBlock(Block b) {
        for (RegistryObject<Block> registryObject : RegistryHandler.BLOCKS.getEntries()) {
            if (registryObject.get() == b) {
                return true;
            }
        }
        return false;
    }

    public Block getBlock(BlockPos pos) {
        return world.getBlockState(pos).getBlock();
    }

    public boolean isWire(BlockPos pos) {
        Block b = getBlock(pos);
        return isElectricBlock(b) && !isSimulationTileEntity(pos);
    }

    public boolean isSimulationTileEntity(BlockPos pos) {
        TileEntity te = world.getChunk(pos).getTileEntity(pos);
        if (te != null && te instanceof SimulationTileEntity) {
            return true;
        }
        return false;
    }

    public SimulationTileEntity getSimulationTileEntity(BlockPos pos) {
        TileEntity te = world.getChunk(pos).getTileEntity(pos);
        if (te != null && te instanceof SimulationTileEntity) {
            return (SimulationTileEntity) te;
        }
        return null;
    }

    public List<BlockPos> getSurroundingBlocks(BlockPos pos) {
        List<BlockPos> list = new ArrayList<>();

        BlockPos up = pos.up();
        if (World.isValid(up)) list.add(up);
        BlockPos down = pos.down();
        if (World.isValid(down)) list.add(down);
        BlockPos north = pos.north();
        if (World.isValid(north)) list.add(north);
        BlockPos south = pos.south();
        if (World.isValid(south)) list.add(south);
        BlockPos east = pos.east();
        if (World.isValid(east)) list.add(east);
        BlockPos west = pos.west();
        if (World.isValid(west)) list.add(west);

        return list;
    }

    /**
     * Class just used for creating a queue of block positions and their
     * corresponding connections.
     */
    private class ConnectedBlock {
        
        public BlockPos pos;
        public SimulationConnection currConnection = null;
        public SimulationTileEntity ste = null;
        public ConnectedBlock previousBlock = null;

        public ConnectedBlock(SimulationTileEntity ste) {
            this.ste = ste;
            this.pos = ste.getPos();
        }

        public ConnectedBlock(BlockPos pos) {
            this.pos = pos;
            this.ste = getSimulationTileEntity(pos);
        }

        public boolean hasSimTileEntity() {
            return ste != null;
        }

        public boolean hasConnection() {
            return currConnection != null;
        }

        public boolean previousBlockEquals(BlockPos newPos) {
            if (newPos == null || previousBlock == null) {
                return false;
            }

            return newPos.equals(previousBlock.pos);
        }

    }

    public void addConnectedBlocks() {
        HashSet<BlockPos> checked = new HashSet<>();
        Queue<ConnectedBlock> unchecked = new LinkedList<>();
        ConnectedBlock initial = new ConnectedBlock(startingBlock);
        unchecked.add(initial);
        
        while (!unchecked.isEmpty()) {
            ConnectedBlock cb = unchecked.remove();
            if (checked.contains(cb.pos)) {
                if (!cb.hasSimTileEntity()) {
                    if (hasPlayer())
                        PlayerUtils.warn(player, "command.electricblocks.requestsimulation.warn_loop");
                    continue;
                }
            }

            if (cb.hasSimTileEntity()) {
                if (!simTileEntities.contains(cb.ste)) simTileEntities.add(cb.ste);

                if (cb.hasConnection()) {
                    cb.currConnection.setToBus(cb.ste.getEmbeddedBus(cb.previousBlock.pos));
                    simConnections.add(cb.currConnection);
                }

                for (BlockPos pos : getSurroundingBlocks(cb.pos)) {
                    if (isWire(pos) && !cb.previousBlockEquals(pos)) {
                        ConnectedBlock ncb = new ConnectedBlock(pos);
                        ncb.previousBlock = cb;
                        ncb.currConnection = new SimulationConnection();
                        ncb.currConnection.setFromBus(cb.ste.getEmbeddedBus(pos));
                        unchecked.add(ncb);
                    }
                }
            } else { // No STE
                if (cb.hasConnection()) {
                    cb.currConnection.incrementLength();
                } else { // No connection
                    ElectricBlocksMod.LOGGER.fatal("No SimulationTileEntity or SimulationConnection for block " + cb.pos.toString());
                }

                for (BlockPos pos : getSurroundingBlocks(cb.pos)) {
                    if (isElectricBlock(getBlock(pos)) && !cb.previousBlockEquals(pos)) {
                        ConnectedBlock ncb = new ConnectedBlock(pos);
                        ncb.previousBlock = cb;
                        ncb.currConnection = cb.currConnection;
                        unchecked.add(ncb);
                    }
                }
            }
            
            checked.add(cb.pos);
        }
    }

}
