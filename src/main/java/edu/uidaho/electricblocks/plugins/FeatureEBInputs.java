package edu.uidaho.electricblocks.plugins;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import mcp.mobius.waila.api.*;
import edu.uidaho.electricblocks.lib.Feature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import edu.uidaho.electricblocks.simulation.SimulationProperty;
import java.util.Map;
import java.util.List;

//HWYLA plugin - displays block power in the HWYLA tooltip.
public class FeatureEBInputs extends Feature implements IComponentProvider {

    //found in en_us.json
    private static final ResourceLocation ENABLED = new ResourceLocation("electricblocks", "ebinputs");



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
                    for (Map.Entry<String, SimulationProperty> entry : ((SimulationTileEntity) tile).getInputs().entrySet()) {
                        if (entry.getValue().getPropertyType() != SimulationProperty.PropertyType.DOUBLE) {
                            //Skip over special properties that are not doubles
                            continue;
                        }
                        final String s = entry.getValue().getLabel() + ": " + entry.getValue().getDouble() + " " + entry.getValue().getUnits();
                        info.add(this.getInfoComponent("ebinputs", s));
                    }
                }
            }

            catch (final Exception e) {

                ElectricBlocksMod.LOGGER.error("Failed to get EBInputs for block {}.", accessor.getBlockState());
                ElectricBlocksMod.LOGGER.catching(e);
            }
        }
    }
}
