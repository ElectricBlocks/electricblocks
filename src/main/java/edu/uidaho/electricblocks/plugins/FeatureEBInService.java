package edu.uidaho.electricblocks.plugins;

import mcp.mobius.waila.api.*;
import edu.uidaho.electricblocks.EBHWYLA;
import edu.uidaho.electricblocks.lib.Feature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import edu.uidaho.electricblocks.blocks.*;
import net.minecraft.block.Block;

import java.util.List;

public class FeatureEBInService extends Feature implements IComponentProvider {

    private static final ResourceLocation ENABLED = new ResourceLocation("wawla", "hardness");

    @Override
    public void initialize (IRegistrar hwyla) {

        hwyla.addConfig(ENABLED, true);
        hwyla.registerComponentProvider(this, TooltipPosition.BODY, Block.class);
    }

    @Override
    public void appendBody (List<ITextComponent> info, IDataAccessor accessor, IPluginConfig config) {

        if (config.get(ENABLED)) {

            try {
                info.add(this.getInfoComponent("ebinservice", "Test FeatureEBInService"));
            }

            catch (final Exception e) {

                EBHWYLA.LOG.error("Failed to get EBInService for block {}.", accessor.getBlockState());
                EBHWYLA.LOG.catching(e);
            }
        }
    }
}
