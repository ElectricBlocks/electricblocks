package edu.uidaho.electricblocks.containers;

import javax.annotation.Nonnull;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.tileentities.ElecFurnaceTileEntity;
import edu.uidaho.electricblocks.utils.FunctionalIntReferenceHolder;
import jdk.nashorn.internal.runtime.regexp.joni.constants.RegexState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class ElecFurnaceContainer extends Container {

    public ElecFurnaceTileEntity tileEntity;
    private IWorldPosCallable canInteractWithCallable;
    public FunctionalIntReferenceHolder currentSmeltTime;

    // Server Constructor
    public ElecFurnaceContainer(final int windowID, final PlayerInventory playerInv,
                                   final ElecFurnaceTileEntity tile) {

        super(RegistryHandler.ELEC_FURNACE_CONTAINER.get(), windowID);

        this.tileEntity = tile;
        this.canInteractWithCallable = IWorldPosCallable.of(tile.getWorld(), tile.getPos());

        final int slotSizePlus2 = 18;
        final int startX = 8;

        // Hotbar
        int hotbarY = 142;
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInv, column, startX + (column * slotSizePlus2), hotbarY));
        }

        // Main Player Inventory
        final int startY = 84;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInv, 9 + (row * 9) + column, startX + (column * slotSizePlus2),
                        startY + (row * slotSizePlus2)));
            }
        }

        // Furnace Slots
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 56, 34));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 1, 116, 35));

        this.trackInt(currentSmeltTime = new FunctionalIntReferenceHolder(() -> this.tileEntity.currentSmeltTime,
                value -> this.tileEntity.currentSmeltTime = value));
        //ElectricBlocksMod.LOGGER.warn("Server side constructor called!");
    }

    // Client Constructor
    public ElecFurnaceContainer(final int windowID, final PlayerInventory playerInv, final PacketBuffer data) {
        this(windowID, playerInv, getTileEntity(playerInv, data));
        //ElectricBlocksMod.LOGGER.warn("Client side constructor called!");

    }

    private static ElecFurnaceTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
        Objects.requireNonNull(playerInv, "playerInv cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (tileAtPos instanceof ElecFurnaceTileEntity) {
            return (ElecFurnaceTileEntity) tileAtPos;
        }
        throw new IllegalStateException("TileEntity is not correct " + tileAtPos);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, RegistryHandler.ELEC_FURNACE_BLOCK.get());
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity player, final int index) {
        //ElectricBlocksMod.LOGGER.warn("Inside transferStackInSlot function with index: ", index);
        ItemStack returnStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack slotStack = slot.getStack();
            returnStack = slotStack.copy();

            final int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (slotStack.getCount() == returnStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        if(index == 0){
            //call tick method
            //ElectricBlocksMod.LOGGER.warn("Move detected in input slot");
            //this.tileEntity.process(); THIS DOES NOT WORK
        }
        return returnStack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSmeltProgressionScaled() {
        return this.currentSmeltTime.get() != 0
                ? this.currentSmeltTime.get() * 24 / this.tileEntity.maxSmeltTime
                : 0;
    }
}
