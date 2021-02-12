package edu.uidaho.electricblocks;

import edu.uidaho.electricblocks.utils.ClientUtils;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uidaho.electricblocks.eventhandlers.BlockEventHandler;
import edu.uidaho.electricblocks.simulation.SimulationHandler;

/**
 * ElectricBlocks main class. This is what gets everything started.
 */
@Mod("electricblocks")
public class ElectricBlocksMod {

    public static final String MOD_ID = "electricblocks"; // Easy to access copy of Mod ID

    public static final Logger LOGGER = LogManager.getLogger(); // Reference to L4J Logger

    // Creates a new tab group in the creative menu for all the blocks and items in
    // this mod
    public static final ItemGroup TAB = new ItemGroup("ebtab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.PORTABLE_GENERATOR_ITEM.get());
        }
    };

    public ElectricBlocksMod() {
        // Register config file
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ElectricBlocksConfig.SERVER_SPEC);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new BlockEventHandler());

        RegistryHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM PREINIT");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientUtils::registerConfigScreen);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        boolean keepAliveSuccessful = false;
        try {
            keepAliveSuccessful = SimulationHandler.instance().sendKeepAlive();
        } catch (Exception e) {
            keepAliveSuccessful = true; // Set this to prevent double message from appearing
            LOGGER.fatal("Unable to contact simulation server! ElectricBlocksMod will be unable to function.");
            LOGGER.fatal("Ensure that EBPP is online and accessible. Also validate your configuration file.");
        }

        if (!keepAliveSuccessful) {
            LOGGER.fatal("Received unexpected response from simulation server. Are you sure your configuration is correct?");
            LOGGER.fatal("Another service other than EBPP may be running at the configured host and port!");
        }
    }

}
