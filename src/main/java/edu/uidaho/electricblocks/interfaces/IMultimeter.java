package edu.uidaho.electricblocks.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface IMultimeter {

    void updateOrToggle(PlayerEntity player);

    void viewOrModify(PlayerEntity player);
}
