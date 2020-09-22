package edu.uidaho.electricblocks.blocks;

import edu.uidaho.electricblocks.tileentities.LampTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        // TODO Make a menu popup for light when right clicked
        LampTileEntity lampTileEntity = (LampTileEntity) worldIn.getTileEntity(pos);
        lampTileEntity.toggleInService();
        return ActionResultType.SUCCESS;
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
