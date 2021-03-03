package edu.uidaho.electricblocks;

import edu.uidaho.electricblocks.blocks.ExternalGridBlock;
import edu.uidaho.electricblocks.blocks.LampBlock;
import edu.uidaho.electricblocks.blocks.WireBlock;
import edu.uidaho.electricblocks.items.ExternalGridItem;
import edu.uidaho.electricblocks.items.LampItem;
import edu.uidaho.electricblocks.items.MultimeterItem;
import edu.uidaho.electricblocks.items.WireItem;
import edu.uidaho.electricblocks.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * This is a static class that cannot be instantiated and is just used to register all of the blocks, items and tile
 * entities. This class is useful for checking if a block or item is a certain type without having to use instanceof.
 */
public final class RegistryHandler {

    private RegistryHandler() {} // Class cannot be instantiated

    // Registers
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ElectricBlocksMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ElectricBlocksMod.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ElectricBlocksMod.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> LAMP_BLOCK = BLOCKS.register("lamp_block", LampBlock::new);
    public static final RegistryObject<Block> EXTERNAL_GRID_BLOCK = BLOCKS.register("externalgrid_block", ExternalGridBlock::new);
    public static final RegistryObject<Block> WIRE_BLOCK = BLOCKS.register("wire_block", WireBlock::new);

    // Items
    public static final RegistryObject<Item> LAMP_ITEM = ITEMS.register("lamp_item", LampItem::new);
    public static final RegistryObject<Item> MULTIMETER_ITEM = ITEMS.register("multimeter_item", MultimeterItem::new);
    public static final RegistryObject<Item> EXTERNAL_GRID_ITEM = ITEMS.register("externalgrid_item", ExternalGridItem::new);
    public static final RegistryObject<Item> WIRE_ITEM = ITEMS.register("wire_item", WireItem::new);

    // Tile Entities
    public static final RegistryObject<TileEntityType<LampTileEntity>> LAMP_TILE_ENTITY  =
            TILE_ENTITIES.register("lamp_tileentity", () -> TileEntityType.Builder.create(LampTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<ExternalGridTileEntity>> EXTERNAL_GRID_TILE_ENTITY =
            TILE_ENTITIES.register("externalgrid_tileentity", () -> TileEntityType.Builder.create(ExternalGridTileEntity::new).build(null));

    /**
     * Initializes all of the registers with the event mod bus so that blocks, items, and tile entities will be
     * recognized by the Forge mod loader
     */
    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
