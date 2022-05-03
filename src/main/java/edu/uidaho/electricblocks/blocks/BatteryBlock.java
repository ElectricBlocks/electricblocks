package edu.uidaho.electricblocks.blocks;

import edu.uidaho.electricblocks.tileentities.BatteryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BatteryBlock extends Block {

    public BatteryBlock() {
        super(Properties.create(Material.EARTH)
                .hardnessAndResistance(0f)
                .sound(SoundType.WOOD)
                .slipperiness(0.6f)
                .harvestLevel(2)
        );
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BatteryTileEntity();
    }
}
