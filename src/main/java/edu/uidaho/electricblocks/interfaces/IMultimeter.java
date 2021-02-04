package edu.uidaho.electricblocks.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface IMultimeter {

    /**
     * Called when a player left clicks on an electric block implementing the IMultimeter interface when the player is
     * holding the multimeter item. Either the block or the tile entity can be implement this interface depending on the
     * required actions. This should update the block or toggle it on or off.
     * @param player The player that clicked on the block
     */
    void updateOrToggle(PlayerEntity player);

    /**
     * Called when a player right clicks on an electric block implementing the IMultimeter interface when the player is
     * holding the multimeter item. Either the block or tile entity can implement this interface depending on the
     * required actions. This should allow players to view/modify information about the block/tile entity. This is
     * usually done using a GUI screen, but could be implemented in other ways depending on the nature of the block
     * @param player The player the clicked on the block
     */
    void viewOrModify(PlayerEntity player);
}
