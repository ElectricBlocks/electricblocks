package edu.uidaho.electricblocks.plugins;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import mcp.mobius.waila.api.*;
import edu.uidaho.electricblocks.lib.Feature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;

import java.util.List;

public class FeatureEBInService extends Feature implements IComponentProvider {

    private static final ResourceLocation ENABLED = new ResourceLocation("electricblocks", "ebinservice");

    @Override
    public void initialize (IRegistrar hwyla) {

        hwyla.addConfig(ENABLED, true);
        hwyla.registerComponentProvider(this, TooltipPosition.BODY, Block.class);
    }

    @Override
    public void appendBody (List<ITextComponent> info, IDataAccessor accessor, IPluginConfig config) {

        if (config.get(ENABLED)) {

            try {
                final TileEntity tile = accessor.getTileEntity();
                if (tile instanceof SimulationTileEntity) {
                    final boolean isinservice = ((SimulationTileEntity) tile).isInService();
                    info.add(this.getInfoComponent("ebinservice", isinservice));
                }
            }

            catch (final Exception e) {

                ElectricBlocksMod.LOGGER.error("Failed to get EBInService for block {}.", accessor.getBlockState());
                ElectricBlocksMod.LOGGER.catching(e);
            }
        }
    }
}
