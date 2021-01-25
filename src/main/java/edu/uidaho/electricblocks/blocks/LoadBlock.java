package edu.uidaho.electricblocks.blocks;

import javax.annotation.Nullable;

import edu.uidaho.electricblocks.tileentities.LoadTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

/**
 * LoadBlock class represents a generic single phase load which has no real
 * actions in game. This can be used to represent loads for which the real life
 * equivalent does not exist within the minecraft game.
 */
public class LoadBlock extends Block {

    public LoadBlock() {
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
        return new LoadTileEntity();
    }
}
