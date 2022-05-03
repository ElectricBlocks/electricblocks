package edu.uidaho.electricblocks.utils;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import edu.uidaho.electricblocks.guis.ElecFurnaceScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ElectricBlocksMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(RegistryHandler.ELEC_FURNACE_CONTAINER.get(), ElecFurnaceScreen::new);
    }
}
