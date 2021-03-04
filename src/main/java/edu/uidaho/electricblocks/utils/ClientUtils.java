package edu.uidaho.electricblocks.utils;

import edu.uidaho.electricblocks.guis.*;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;

/**
 * This class is used to hold any references to client side only code. This is critical for dedicated servers to run.
 * If client side code is called on a dedicated server, it will cause a crash. Any calls to functions in this class MUST
 * be wrapped using a DistExecutor call. For example:
 *
 * DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientUtils.openTransformerScreen(this, player));
 */
public class ClientUtils {

    private ClientUtils() {}

    public static void openSTEScreen(SimulationTileEntity simulationTileEntity, PlayerEntity playerEntity) {
        Minecraft.getInstance().displayGuiScreen(new STEScreen(simulationTileEntity, playerEntity));
    }

    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen());
    }

}
