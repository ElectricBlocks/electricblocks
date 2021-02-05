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

import javax.annotation.Nullable;

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
        asyncBlocksThread = new Thread(() -> {
            addConnectedBlocks();
            setReady();
        });
        asyncBlocksThread.start();
    }

    /**
     * Gets the list of SimulationTileEntities in this network
     * @return The list of SimulationTileEntities in this network
     */
    public List<SimulationTileEntity> getSimulationList() {
        return simTileEntities;
    }

    /**
     * Gets the list of SimulationConnections in this network
     * @return The list of SimulationConnections in this network
     */
    public List<SimulationConnection> getSimulationConnections() {
        return simConnections;
    }

    /**
     * Extracts simulation results that were received from the EBPP server. This function is called by the
     * SimulationHandler when a simulation was successfully performed and the results are available for updating the
     * SimulationTileEntities in this network.
     * @param simResults The JSON results of the simulation request
     */
    public void handleSimulationResults(JsonObject simResults) {
        JsonObject elements = simResults.get("elements").getAsJsonObject();
        for (SimulationTileEntity sim : simTileEntities) {
            sim.receiveSimulationResults(elements.get(sim.getSimulationID().toString()).getAsJsonObject());
        }
    }

    /**
     * This function loops through all of the SimulationTileEntities in the network and calls their zeroSim function.
     * This function is called whenever a simulation involving this SimulationNetwork fails for any reason. This sets
     * all the results to zero. This does not zero out any of the inputs, only the results
     */
    public void zeroSimResults() {
        for (SimulationTileEntity sim : simTileEntities) {
            sim.zeroSim();
        }
    }

    /**
     * Marks this SimulationNetwork as ready for simulation. This indicates to the SimulationHandler that this network
     * has finished being constructed and can be sent to the EBPP simulation server.
     */
    private void setReady() {
        ready = true;
    }

    /**
     * Checks whether or not this SimulationNetwork is ready to be simulated. SimulationNetworks are built in a separate
     * async thread and so this function will determine whether or not this process has been completed.
     * @return Whether or not this SimulationNetwork is ready to be simulated.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Sets the player that triggered the creation of this simulation network
     * @param player The player that triggered the creation of this simulation network
     */
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    /**
     * Gets the player that triggered this SimulationNetwork to be created if they exist
     * @return The player that triggered this SimulationNetwork to be created or null if they don't exist
     */
    @Nullable
    public PlayerEntity getPlayer() {
        return this.player;
    }

    /**
     * Checks if this SimulationNetwork has a known player that triggered it.
     * @return Whether or not this SimulationNetwork has a known player that triggered it
     */
    public boolean hasPlayer() {
        return this.player != null;
    }

    /**
     * Checks if the block is an electric block. This function helps differentiate between blocks specific to this mod
     * and all the other blocks in the game
     * @param b The block to check
     * @return Whether or not the block is an electric block
     */
    public boolean isElectricBlock(Block b) {
        for (RegistryObject<Block> registryObject : RegistryHandler.BLOCKS.getEntries()) {
            if (registryObject.get() == b) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the block at the specified position. This is mainly useful for comparing block types as blocks in game are
     * not actually represented by an instance of the Block class just provides the archetype for a block.
     * @param pos The position to check
     * @return The block at the specified position
     */
    public Block getBlock(BlockPos pos) {
        return world.getBlockState(pos).getBlock();
    }

    /**
     * Checks whether or not the block at a specified position is a wire/line block
     * @param pos The block position to check
     * @return Whether or not the block at the specified position is a wire/line block
     */
    public boolean isWire(BlockPos pos) {
        Block b = getBlock(pos);
        return isElectricBlock(b) && !isSimulationTileEntity(pos);
    }

    /**
     * Checks if a SimulationTileEntity is attached to the specified block position.
     * @param pos The block position to check
     * @return Whether or not a SimulationTileEntity is attached to the specified block position
     */
    public boolean isSimulationTileEntity(BlockPos pos) {
        TileEntity te = world.getChunk(pos).getTileEntity(pos);
        return te instanceof SimulationTileEntity;
    }

    /**
     * Gets the SimulationTileEntity at a specific block position by checking the world for a tile entity at that
     * position and casting to SimulationTileEntity if it is an instance of one. Will return null if there is no tile
     * entity there or if it is a different kind of tile entity.
     * @param pos The block position to check for a SimulationTileEntity
     * @return The SimulationTileEntity at the specified location or null if it doesn't exist.
     */
    @Nullable
    public SimulationTileEntity getSimulationTileEntity(BlockPos pos) {
        TileEntity te = world.getChunk(pos).getTileEntity(pos);
        if (te instanceof SimulationTileEntity) {
            return (SimulationTileEntity) te;
        }
        return null;
    }

    /**
     * Gets all the blocks surrounding a specific block position that are one block away. Diagonals are not included so
     * this will GENERALLY be 6 blocks long corresponding to the blocks offset by one to the north, south, east, west
     * up, and down directions. This may be different though if the player is at the very extremes of a map such as near
     * bedrock or at the build height limit
     * @param pos The starting base position from which the surrounding blocks are offset
     * @return The list of all blocks surrounding a specific block position that are at most one block away
     */
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
     * Class just used for creating a queue of block positions and their corresponding connections. This creates a sort
     * of linked list or directed graph structure that allows us to construct the SimulationNetwork
     */
    private class ConnectedBlock {
        
        public BlockPos pos;
        public SimulationConnection currConnection = null;
        public SimulationTileEntity ste;
        public ConnectedBlock previousBlock = null;

        public ConnectedBlock(SimulationTileEntity ste) {
            this.ste = ste;
            this.pos = ste.getPos();
        }

        public ConnectedBlock(BlockPos pos) {
            this.pos = pos;
            this.ste = getSimulationTileEntity(pos);
        }

        /**
         * Checks whether or not this block has a SimulationTileEntity attached to it.
         * @return Whether or not this block has a SimulationTileEntity attached to it.
         */
        public boolean hasSimTileEntity() {
            return ste != null;
        }

        /**
         * Checks whether this block has an existing connection.
         * @return Whether or not this block has an existing connection.
         */
        public boolean hasConnection() {
            return currConnection != null;
        }

        /**
         * Verifies that the previous block in the graph is not the same as the previous block. This helps prevent the
         * network from backtracking while scanning the surrounding blocks.
         * @param newPos The block position that is currently being checked. May be null.
         * @return True if newPos is not equal to the previous block in this object. False if the two blocks are equal.
         * Also returns true if either the previous block or the new block position are null
         */
        public boolean previousBlockNotEqualTo(@Nullable BlockPos newPos) {
            if (newPos == null || previousBlock == null) {
                return true;
            }

            return !newPos.equals(previousBlock.pos);
        }

    }

    /**
     * This function is used to identify all the electric blocks that are connected to this SimulationNetwork's starting
     * block and how it is connected to them. It does this using a list of checked and unchecked blocks. The algorithm
     * continually checks the blocks surrounding the current block and uses this information to update the network's
     * list of connections and SimulationTileEntities.
     */
    public void addConnectedBlocks() {
        HashSet<BlockPos> checked = new HashSet<>();
        Queue<ConnectedBlock> unchecked = new LinkedList<>();
        ConnectedBlock initial = new ConnectedBlock(startingBlock); // Initialize starting block
        unchecked.add(initial); // Add starting block to the list
        
        while (!unchecked.isEmpty()) { // Repeat loop until the list of unchecked blocks is empty
            ConnectedBlock cb = unchecked.remove();
            if (checked.contains(cb.pos)) { // Ignore blocks that have already been checked
                if (!cb.hasSimTileEntity()) {
                    /*
                     * If a block has already been checked and it doesn't have a simulation tile entity, that means that
                     * we have found a loop in the network where one bus is connected to itself. This is not allowed by
                     * PandaPower and so we have to discard this loop. The player is warned if a player exists.
                     */
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
                    if (isWire(pos) && cb.previousBlockNotEqualTo(pos)) {
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
                    if (isElectricBlock(getBlock(pos)) && cb.previousBlockNotEqualTo(pos)) {
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
