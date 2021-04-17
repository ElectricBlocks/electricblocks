package edu.uidaho.electricblocks.network;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Server-side only packet handler class that opens a network chanel and listens for STE updates. This allows
 * client-side GUI to communicate changes to the server.
 */
public class ElectricBlocksPacketHandler {

    private static int packetId = 1;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("electricblocks", "ste_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    /**
     * Function called by network channel when an STE update packet is received
     * @param message The parsed TileEntityMessageToServer
     * @param context The network context
     */
    public static void onTileEntityMessageReceived(final TileEntityMessageToServer message, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.setPacketHandled(true);
        if (ctx.getDirection().getReceptionSide() != LogicalSide.SERVER) {
            ElectricBlocksMod.LOGGER.warn("Received a TileEntityMessage on the wrong side. Discarding packet.");
            return;
        }
        if (!message.isValid()) {
            ElectricBlocksMod.LOGGER.warn("Unable to parse TileEntityMessage! Discarding packet.");
            return;
        }

        ServerPlayerEntity player = ctx.getSender();
        if (player == null) { // Make sure a player sent the message
            ElectricBlocksMod.LOGGER.warn("Received a TileEntityMessage without a player! Discarding packet.");
            return;
        }

        ctx.enqueueWork(() -> processMessage(message, player));
    }

    /**
     * This function is called when the server has received a packet from a client and wants to process it.
     * @param message The TileEntityMessageToServer to be processed
     * @param player The player that sent the message
     */
    static void processMessage(final TileEntityMessageToServer message, ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        BlockPos pos = new BlockPos(message.getX(), message.getY(), message.getZ());
        double[] inputs = message.getInputs();
        // Verify that the block position
        if (!World.isValid(pos) || !world.isBlockModifiable(player, pos) || !player.getPosition().withinDistance(pos, 5.0)) {
            PlayerUtils.error(player, "command.electricblocks.viewmodify.err_block");
            return;
        }

        TileEntity te = world.getTileEntity(pos);
        SimulationTileEntity ste = te instanceof SimulationTileEntity ? (SimulationTileEntity) te : null;

        if (ste != null) {
            ste.readPacketBuffer(inputs);
        } else {
            PlayerUtils.error(player, "command.electricblocks.viewmodify.err_block");
            return;
        }

        ste.setInService(message.isInService());
        ste.requestSimulation(player);
    }

    /**
     * Registers packets and associated methods with communications channel
     */
    public static void registerPackets() {
        INSTANCE.registerMessage(packetId++, TileEntityMessageToServer.class,
                TileEntityMessageToServer::encode,
                TileEntityMessageToServer::decode,
                ElectricBlocksPacketHandler::onTileEntityMessageReceived,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

}
