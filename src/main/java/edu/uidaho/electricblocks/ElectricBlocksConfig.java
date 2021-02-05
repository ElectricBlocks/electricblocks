package edu.uidaho.electricblocks;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
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
    private static boolean updateOnBlockBreak;
    private static boolean logJSONRequests;

    /**
     * Get the host URL of the EBPP simulation server endpoint. This is used by the simulation handler to create
     * connections for processing simulation requests
     * @return The host URL of the EBPP endpoint
     */
    public static String getHostURL() {
        return hostURL;
    }

    /**
     * Whether or not the simulation should be updated when an electric block is broken. If this is false then the
     * in game state may fall out of sync with what would happen in real life. This should usually be true unless a
     * large electrical network is being broken down as this would cause a new simulation request for every single
     * block that is broken.
     * @return Whether or not the simulation should be updated when an electric block is broken.
     */
    public static boolean getUpdateOnBlockBreak() {
        return updateOnBlockBreak;
    }

    /**
     * Whether or not JSON requests sent by the simulation handler should be logged. This can usually be left off but is
     * useful for development and debugging purposes.
     * @return Should JSON requests be logged
     */
    public static boolean getLogJSONRequests() {
        return logJSONRequests;
    }

    public static void bakeConfig() {
        hostURL = SERVER.hostURL.get();
        updateOnBlockBreak = SERVER.updateOnBlockBreak.get();
        logJSONRequests = SERVER.logJSONRequests.get();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == ElectricBlocksConfig.SERVER_SPEC) {
            bakeConfig();
        }
    }

    public static class ServerConfig {

        public final ConfigValue<String> hostURL;
        public final BooleanValue updateOnBlockBreak;
        public final BooleanValue logJSONRequests;
        
        public ServerConfig(ForgeConfigSpec.Builder builder) {
            hostURL = builder
                .comment("Host of EBPP Simulation Server including port and endpoint")
                .translation("config.electricblocks.hostURL")
                .define("hostURL", "http://127.0.0.1:1127/api");
            updateOnBlockBreak = builder
                .comment("Whether or not the simulation should be updated when an electric block is broken")
                .translation("config.electricblocks.update_on_block_break")
                .define("updateOnBlockBreak", true);
            logJSONRequests = builder
                .comment("Should JSON requests to EBPP be included in the log. Useful for debugging")
                .translation("config.electricblocks.log_json_requests")
                .define("logJSONRequests", false);
        }

    }
    
}
