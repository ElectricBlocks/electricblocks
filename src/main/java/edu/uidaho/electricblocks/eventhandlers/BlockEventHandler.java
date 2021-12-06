package edu.uidaho.electricblocks.eventhandlers;

import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.ElectricBlocksConfig;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * This class holds methods called by Forge API when block based events are triggered. This is mostly used to make the
 * multimeter item function correctly.
 */
@Mod.EventBusSubscriber
public class BlockEventHandler {

    /**
     * This function is called by Forge API when a player left clicks on a block. This is used to detect when a player
     * left clicks on a block with the multimeter in hand so the toggle/update operation can be performed.
     * @param event The event object that triggered this function call
     */
    @SubscribeEvent
    public void blockLeftClicked(PlayerInteractEvent.LeftClickBlock event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();

        Item item = player.getHeldItem(Hand.MAIN_HAND).getItem();
        if (item == RegistryHandler.MULTIMETER_ITEM.get() || item == RegistryHandler.MULTIMETER_ITEM2.get() || item == RegistryHandler.MULTIMETER_ITEM3.get()) { // Only process if player is holding multimeter
            BlockState blockState = world.getBlockState(event.getPos());
            Block block = blockState.getBlock();

            IMultimeter multimeter;
            TileEntity te;
            // Both blocks and tile entities can implement the IMultimeter interface
            if (block instanceof IMultimeter) {
                multimeter = (IMultimeter) block;
            } else if ((te = world.getTileEntity(event.getPos())) instanceof IMultimeter) {
                multimeter = (IMultimeter) te;
            } else {
                // Player clicked on a block that doesn't implement interface
                PlayerUtils.error(event.getPlayer(), "command.electricblocks.updatetoggle.err_invalid_block");
                return;
            }

            PlayerUtils.sendMessage(player, "command.electricblocks.updatetoggle");
            multimeter.updateOrToggle(player);
        }
    }

    /**
     * This function is called by the Forge API when a player right clicks on a block. This is used to detect when the
     * player right clicks on a block with the multimeter item in hand so the view/modify operation can be performed.
     * @param event The event object that triggered this function call.
     */
    @SubscribeEvent
    public void blockRightClicked(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();

        Item item = player.getHeldItem(Hand.MAIN_HAND).getItem();
        if (item == RegistryHandler.MULTIMETER_ITEM.get() || item == RegistryHandler.MULTIMETER_ITEM2.get() || item == RegistryHandler.MULTIMETER_ITEM3.get()) { // Only process if player is holding multimeter
            BlockState blockState = world.getBlockState(event.getPos());
            Block block = blockState.getBlock();

            IMultimeter multimeter;
            TileEntity te;
            // Both blocks and tile entities can implement the IMultimeter interface
            if (block instanceof IMultimeter) {
                multimeter = (IMultimeter) block;
            } else if ((te = world.getTileEntity(event.getPos())) instanceof IMultimeter) {
                multimeter = (IMultimeter) te;
            } else {
                // Player clicked on a block that doesn't implement interface
                PlayerUtils.error(event.getPlayer(), "command.electricblocks.viewmodify.err_invalid_block");
                return;
            }

            multimeter.viewOrModify(player);
        }
    }

    /**
     * This funciton is called by the Forge API when a player breaks a block. This is used to prevent the update/toggle
     * action from breaking blocks when players left click on an electric block in game.
     * @param event The event object that triggered this function call
     */
    @SubscribeEvent
    public void blockBroken(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        Item item = player.getHeldItem(Hand.MAIN_HAND).getItem();
        BlockPos pos = event.getPos();

        if (item == RegistryHandler.MULTIMETER_ITEM.get()) { // Cancel event if player is holding multimeter
            event.setCanceled(true);
            return;
        }

        if (ElectricBlocksConfig.getUpdateOnBlockBreak()) { // Request simulation if configured to do so
            TileEntity te;
            if ((te = event.getWorld().getTileEntity(pos)) instanceof SimulationTileEntity) {
                PlayerUtils.warn(player, "command.electricblocks.block_broken");
                SimulationTileEntity simulationTileEntity = (SimulationTileEntity) te;
                simulationTileEntity.disable();
                simulationTileEntity.requestSimulation(player);
            }
        }
    }

}
