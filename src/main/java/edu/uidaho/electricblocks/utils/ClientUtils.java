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

    private ClientUtils() {} // Class is not instantiable and just holds static methods

    /**
     * Opens a new GUI screen for player to interact with a STE
     * @param simulationTileEntity The STE to display
     * @param playerEntity The player to display the STEScreen to
     */
    public static void openSTEScreen(SimulationTileEntity simulationTileEntity, PlayerEntity playerEntity) {
        Minecraft.getInstance().displayGuiScreen(new STEScreen(simulationTileEntity, playerEntity));
    }

    /**
     * Registers client-side config screen with Forge so it can be opened from the mods menu
     */
    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen());
    }

}
