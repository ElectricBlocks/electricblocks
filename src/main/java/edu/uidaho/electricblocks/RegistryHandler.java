package edu.uidaho.electricblocks;

import edu.uidaho.electricblocks.blocks.*;
import edu.uidaho.electricblocks.containers.ElecFurnaceContainer;
import edu.uidaho.electricblocks.items.*;
import edu.uidaho.electricblocks.tileentities.*;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import static edu.uidaho.electricblocks.init.RecipeSerializerInit.RECIPE_SERIALIZERS;

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
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(
            ForgeRegistries.CONTAINERS, ElectricBlocksMod.MOD_ID);



    // Blocks
    public static final RegistryObject<Block> LAMP_BLOCK = BLOCKS.register("lamp_block", LampBlock::new);
    public static final RegistryObject<Block> BATTERY_BLOCK = BLOCKS.register("battery_block", BatteryBlock::new);
    public static final RegistryObject<Block> EXTERNAL_GRID_BLOCK = BLOCKS.register("externalgrid_block", ExternalGridBlock::new);
    public static final RegistryObject<Block> WIRE_BLOCK = BLOCKS.register("wire_block", WireBlock::new);
    public static final RegistryObject<Block> BUS_BLOCK = BLOCKS.register("bus_block", BusBlock::new);
    public static final RegistryObject<Block> GENERATOR_BLOCK = BLOCKS.register("generator_block", GeneratorBlock::new);
    public static final RegistryObject<Block> LOAD_BLOCK = BLOCKS.register("load_block", LoadBlock::new);
    public static final RegistryObject<Block> TRANSFORMER_BLOCK = BLOCKS.register("transformer_block", TransformerBlock::new);
    public static final RegistryObject<Block> ELEC_FURNACE_BLOCK = BLOCKS.register("elec_furnace_block", ElecFurnaceBlock::new);

    // Items
    public static final RegistryObject<Item> LAMP_ITEM = ITEMS.register("lamp_item", LampItem::new);
    public static final RegistryObject<Item> BATTERY_ITEM = ITEMS.register("battery_item", BatteryItem::new);
    public static final RegistryObject<Item> MULTIMETER_ITEM = ITEMS.register("multimeter_item", MultimeterItem::new);
    public static final RegistryObject<Item> MULTIMETER_ITEM2 = ITEMS.register("multimeter_item2", MultimeterItem2::new);
    public static final RegistryObject<Item> MULTIMETER_ITEM3 = ITEMS.register("multimeter_item3", MultimeterItem3::new);
    public static final RegistryObject<Item> EXTERNAL_GRID_ITEM = ITEMS.register("externalgrid_item", ExternalGridItem::new);
    public static final RegistryObject<Item> WIRE_ITEM = ITEMS.register("wire_item", WireItem::new);
    public static final RegistryObject<Item> BUS_ITEM = ITEMS.register("bus_item", BusItem::new);
    public static final RegistryObject<Item> GENERATOR_ITEM = ITEMS.register("generator_item", GeneratorItem::new);
    public static final RegistryObject<Item> LOAD_ITEM = ITEMS.register("load_item", LoadItem::new);
    public static final RegistryObject<Item> TRANSFORMER_ITEM = ITEMS.register("transformer_item", TransformerItem::new);
    public static final RegistryObject<Item> ELEC_FURNACE_ITEM = ITEMS.register("elec_furnace_item", ElecFurnaceItem::new);

    // Tile Entities
    public static final RegistryObject<TileEntityType<LampTileEntity>> LAMP_TILE_ENTITY  =
            TILE_ENTITIES.register("lamp_tileentity", () -> TileEntityType.Builder.create(LampTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<BatteryTileEntity>> BATTERY_TILE_ENTITY  =
            TILE_ENTITIES.register("battery_tileentity", () -> TileEntityType.Builder.create(BatteryTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<ExternalGridTileEntity>> EXTERNAL_GRID_TILE_ENTITY =
            TILE_ENTITIES.register("externalgrid_tileentity", () -> TileEntityType.Builder.create(ExternalGridTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<BusTileEntity>> BUS_TILE_ENTITY =
            TILE_ENTITIES.register("bus_tileentity", () -> TileEntityType.Builder.create(BusTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<GeneratorTileEntity>> GENERATOR_TILE_ENTITY =
            TILE_ENTITIES.register("generator_tileentity", () -> TileEntityType.Builder.create(GeneratorTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<LoadTileEntity>> LOAD_TILE_ENTITY =
            TILE_ENTITIES.register("load_tileentity", () -> TileEntityType.Builder.create(LoadTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<TransformerTileEntity>> TRANSFORMER_TILE_ENTITY =
            TILE_ENTITIES.register("transformer_tileentity", () -> TileEntityType.Builder.create(TransformerTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<ElecFurnaceTileEntity>> ELEC_FURNACE_TILE_ENTITY =
            TILE_ENTITIES.register("elec_furnace_tileentity", () -> TileEntityType.Builder.create(ElecFurnaceTileEntity::new).build(null));

    //Containers
    public static final RegistryObject<ContainerType<ElecFurnaceContainer>> ELEC_FURNACE_CONTAINER = CONTAINER_TYPES
            .register("electric_furnace", () -> IForgeContainerType.create(ElecFurnaceContainer::new));

    /**
     * Initializes all of the registers with the event mod bus so that blocks, items, and tile entities will be
     * recognized by the Forge mod loader
     */
    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
