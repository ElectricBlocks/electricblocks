package edu.uidaho.electricblocks;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class EBHWYLAPlugin implements IWailaPlugin {
    @Override
    public void register (IRegistrar hwyla) {

        ElectricBlocksMod.getFeatures().forEach(f -> f.initialize(hwyla));
    }
}
