package edu.uidaho.electricblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BatteryBlock extends Block {

    public BatteryBlock() {
        super(Properties.create(Material.EARTH)
                .hardnessAndResistance(0f)
                .sound(SoundType.ANVIL)
                .slipperiness(0.6f)
                .harvestLevel(2)
        );
    }
}