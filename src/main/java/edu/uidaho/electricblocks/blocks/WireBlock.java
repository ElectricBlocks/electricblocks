package edu.uidaho.electricblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class WireBlock extends Block {

    public WireBlock() {
        super(Block.Properties.create(Material.EARTH)
                .hardnessAndResistance(0f)
                .sound(SoundType.STONE)
                .slipperiness(0.6f)
                .harvestLevel(2)
        );
    }
}