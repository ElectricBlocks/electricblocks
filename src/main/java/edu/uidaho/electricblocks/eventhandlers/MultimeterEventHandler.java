package edu.uidaho.electricblocks.eventhandlers;

import edu.uidaho.electricblocks.interfaces.IMultimeter;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MultimeterEventHandler {
    
    @SubscribeEvent
    public void blockLeftClicked(PlayerInteractEvent.LeftClickBlock event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();

        Item item = player.getHeldItem(Hand.MAIN_HAND).getItem();
        if (item == RegistryHandler.MULTIMETER_ITEM.get()) {
            BlockState blockState = world.getBlockState(event.getPos());
            Block block = blockState.getBlock();

            IMultimeter multimeter = null;
            if (block instanceof IMultimeter) {
                multimeter = (IMultimeter) block;
            } else if (blockState.hasTileEntity() && world.getTileEntity(event.getPos()) instanceof IMultimeter) {
                multimeter = (IMultimeter) world.getTileEntity(event.getPos());
            } else {
                PlayerUtils.error(event.getPlayer(), "command.electricblocks.updatetoggle.err_invalid_block");
                return;
            }

            PlayerUtils.sendMessage(player, "command.electricblocks.updatetoggle");
            multimeter.updateOrToggle(player);
        }
    }

    @SubscribeEvent
    public void blockRightClicked(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();

        Item item = player.getHeldItem(Hand.MAIN_HAND).getItem();
        if (item == RegistryHandler.MULTIMETER_ITEM.get()) {
            BlockState blockState = world.getBlockState(event.getPos());
            Block block = blockState.getBlock();

            IMultimeter multimeter = null;
            if (block instanceof IMultimeter) {
                multimeter = (IMultimeter) block;
            } else if (blockState.hasTileEntity() && world.getTileEntity(event.getPos()) instanceof IMultimeter) {
                multimeter = (IMultimeter) world.getTileEntity(event.getPos());
            } else {
                PlayerUtils.error(event.getPlayer(), "command.electricblocks.viewmodify.err_invalid_block");
                return;
            }

            multimeter.viewOrModify(player);
        }
    }

    @SubscribeEvent
    public void blockBroken(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        Item item = player.getHeldItem(Hand.MAIN_HAND).getItem();

        if (item == RegistryHandler.MULTIMETER_ITEM.get()) {
            event.setCanceled(true);
        }
    }

}
