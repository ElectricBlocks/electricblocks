package edu.uidaho.electricblocks.network;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.tileentities.*;
import edu.uidaho.electricblocks.utils.MetricUnit;
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

public class ElectricBlocksPacketHandler {

    private static int packetId = 1;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("electricblocks", "ste_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

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
        SimulationTileEntity aste = te instanceof SimulationTileEntity ? (SimulationTileEntity) te : null;

        if (aste instanceof ExternalGridTileEntity) {
            ExternalGridTileEntity ste = (ExternalGridTileEntity) aste;
            ste.setSlackVoltage(new MetricUnit(inputs[0]));
            ste.setBusVoltage(new MetricUnit(inputs[1]));
        } else if (aste instanceof GeneratorTileEntity) {
            GeneratorTileEntity ste = (GeneratorTileEntity) aste;
            ste.setMaxPower(new MetricUnit(inputs[0]));
            ste.setPeakVoltage(new MetricUnit(inputs[1]));
            ste.setBusVoltage(new MetricUnit(inputs[2]));
        } else if (aste instanceof LampTileEntity) {
            LampTileEntity ste = (LampTileEntity) aste;
            ste.setMaxPower(new MetricUnit(inputs[0]));
            ste.setBusVoltage(new MetricUnit(inputs[1]));
        } else if (aste instanceof LoadTileEntity) {
            LoadTileEntity ste = (LoadTileEntity) aste;
            ste.setMaxPower(new MetricUnit(inputs[0]));
            ste.setBusVoltage(new MetricUnit(inputs[1]));
        } else if (aste instanceof TransformerTileEntity) {
            TransformerTileEntity ste = (TransformerTileEntity) aste;
            ste.setRatedApparentPower(new MetricUnit(inputs[0]));
            ste.setRatedVoltageAtHighBus(new MetricUnit(inputs[1]));
            ste.setRatedVoltageAtLowBus(new MetricUnit(inputs[2]));
            ste.setShortCircuitVoltagePercent(inputs[3]);
            ste.setShortCircuitVoltageRealComponentPercent(inputs[4]);
            ste.setIronLosses(new MetricUnit(inputs[5]));
            ste.setOpenLoopLossesPercent(inputs[6]);
            ste.setShiftDegree(inputs[7]);
        } else if (aste instanceof BusTileEntity) {
            BusTileEntity ste = (BusTileEntity) aste;
            ste.setBusVoltage(new MetricUnit(inputs[0]));
        } else {
            PlayerUtils.error(player, "command.electricblocks.viewmodify.err_block");
            return;
        }

        aste.setInService(message.isInService());
        aste.requestSimulation(player);
    }

    public static void registerPackets() {
        INSTANCE.registerMessage(packetId++, TileEntityMessageToServer.class,
                TileEntityMessageToServer::encode,
                TileEntityMessageToServer::decode,
                ElectricBlocksPacketHandler::onTileEntityMessageReceived,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

}
