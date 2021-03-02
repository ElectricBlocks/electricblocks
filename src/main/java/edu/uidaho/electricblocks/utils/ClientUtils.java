package edu.uidaho.electricblocks.utils;

import edu.uidaho.electricblocks.guis.*;
import edu.uidaho.electricblocks.tileentities.*;
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

    public static void openExternalGridScreen(ExternalGridTileEntity externalGridTileEntity, PlayerEntity playerEntity) {
        Minecraft.getInstance().displayGuiScreen(new ExternalGridScreen(externalGridTileEntity, playerEntity));
    }

    public static void openGeneratorScreen(GeneratorTileEntity generatorTileEntity, PlayerEntity playerEntity) {
        Minecraft.getInstance().displayGuiScreen(new GeneratorScreen(generatorTileEntity, playerEntity));
    }

    public static void openLampScreen(LampTileEntity lampTileEntity, PlayerEntity playerEntity) {
        Minecraft.getInstance().displayGuiScreen(new LampScreen(lampTileEntity, playerEntity));
    }

    public static void openLoadScreen(LoadTileEntity loadTileEntity, PlayerEntity playerEntity) {
        Minecraft.getInstance().displayGuiScreen(new LoadScreen(loadTileEntity, playerEntity));
    }

    public static void openTransformerScreen(TransformerTileEntity transformerTileEntity, PlayerEntity playerEntity) {
        Minecraft.getInstance().displayGuiScreen(new TransformerScreen(transformerTileEntity, playerEntity));
    }

    public static void openBusScreen(BusTileEntity busTileEntity, PlayerEntity playerEntity) {
        Minecraft.getInstance().displayGuiScreen(new BusScreen(busTileEntity, playerEntity));
    }

    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen());
    }

}
