package edu.uidaho.electricblocks.blocks;

import edu.uidaho.electricblocks.tileentities.LampTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class LampBlock extends Block {

    public LampBlock() {
        super(Block.Properties.create(Material.GLASS)
            .hardnessAndResistance(0f).sound(SoundType.GLASS)
            .sound(SoundType.GLASS)
            .slipperiness(0.6f)
            .harvestLevel(0)
            .lightValue(0)
        );
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LampTileEntity();
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        LampTileEntity lampTileEntity = (LampTileEntity) world.getTileEntity(pos);
        if (lampTileEntity == null || !lampTileEntity.isInService()) {
            return 0;
        }
        return lampTileEntity.getScaledLightValue();
    }



}
