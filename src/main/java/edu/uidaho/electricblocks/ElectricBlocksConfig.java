package edu.uidaho.electricblocks;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

@EventBusSubscriber(modid = ElectricBlocksMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ElectricBlocksConfig {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
                .configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    private static String hostURL;

    public static String getHostURL() {
        return hostURL;
    }

    public static void bakeConfig() {
        hostURL = SERVER.hostURL.get();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == ElectricBlocksConfig.SERVER_SPEC) {
            bakeConfig();
        }
    }

    public static class ServerConfig {

        public final ConfigValue<String> hostURL;
        
        public ServerConfig(ForgeConfigSpec.Builder builder) {
            hostURL = builder
                .comment("Host of EBPP Simulation Server including port and endpoint")
                .translation("config.electricblocks.hostURL")
                .define("hostURL", "http://127.0.0.1:1127/api");
        }

    }
    
}
