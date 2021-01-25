package edu.uidaho.electricblocks;

import net.minecraft.entity.player.PlayerEntity;

public interface IMultimeter {

    public void updateOrToggle(PlayerEntity player);

    public void viewOrModify(PlayerEntity player);
}
