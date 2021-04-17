package edu.uidaho.electricblocks.blocks;

import edu.uidaho.electricblocks.tileentities.ThreePhaseExternalGridTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * This block represents a connection to an external grid for unbalanced power flow calculations.
 */
public class ThreePhaseExternalGridBlock extends Block {

    public ThreePhaseExternalGridBlock() {
        super(Block.Properties.create(Material.EARTH)
                .hardnessAndResistance(0f)
                .sound(SoundType.ANVIL)
                .slipperiness(0.6f)
                .harvestLevel(0)
        );
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ThreePhaseExternalGridTileEntity();
    }
}