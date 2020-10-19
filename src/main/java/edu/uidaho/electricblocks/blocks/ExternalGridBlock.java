package edu.uidaho.electricblocks.blocks;

import edu.uidaho.electricblocks.tileentities.ExternalGridTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * This block represents a connection to an external grid such as what one might receive in a typical home.
 * The defaults for this is 120 volts
 */
public class ExternalGridBlock extends Block {

    public ExternalGridBlock() {
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
        return new ExternalGridTileEntity();
    }
}
