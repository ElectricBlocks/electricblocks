package edu.uidaho.electricblocks;

import edu.uidaho.electricblocks.blocks.ExternalGridBlock;
import edu.uidaho.electricblocks.blocks.LampBlock;
import edu.uidaho.electricblocks.blocks.LoadBlock;
import edu.uidaho.electricblocks.blocks.PortableGeneratorBlock;
import edu.uidaho.electricblocks.blocks.TransformerBlock;
import edu.uidaho.electricblocks.blocks.WireBlock;
import edu.uidaho.electricblocks.blocks.BusBlock;
import edu.uidaho.electricblocks.items.ExternalGridItem;
import edu.uidaho.electricblocks.items.LampItem;
import edu.uidaho.electricblocks.items.LoadItem;
import edu.uidaho.electricblocks.items.MultimeterItem;
import edu.uidaho.electricblocks.items.PortableGeneratorItem;
import edu.uidaho.electricblocks.items.TransformerItem;
import edu.uidaho.electricblocks.items.WireItem;
import edu.uidaho.electricblocks.items.BusItem;
import edu.uidaho.electricblocks.tileentities.ExternalGridTileEntity;
import edu.uidaho.electricblocks.tileentities.GeneratorTileEntity;
import edu.uidaho.electricblocks.tileentities.LampTileEntity;
import edu.uidaho.electricblocks.tileentities.LoadTileEntity;
import edu.uidaho.electricblocks.tileentities.TransformerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

    // Registers
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ElectricBlocksMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ElectricBlocksMod.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ElectricBlocksMod.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> PORTABLE_GENERATOR_BLOCK = BLOCKS.register("portablegenerator_block", PortableGeneratorBlock::new);
    public static final RegistryObject<Block> WIRE_BLOCK = BLOCKS.register("wire_block", WireBlock::new);
    public static final RegistryObject<Block> BUS_BLOCK = BLOCKS.register("bus_block", BusBlock::new);
    public static final RegistryObject<Block> EXTERNAL_GRID_BLOCK = BLOCKS.register("externalgrid_block", ExternalGridBlock::new);
    public static final RegistryObject<Block> LAMP_BLOCK = BLOCKS.register("lamp_block", LampBlock::new);
    public static final RegistryObject<Block> LOAD_BLOCK = BLOCKS.register("load_block", LoadBlock::new);
    public static final RegistryObject<Block> TRANSFORMER_BLOCK = BLOCKS.register("transformer_block", TransformerBlock::new);

    // Items
    public static final RegistryObject<Item> PORTABLE_GENERATOR_ITEM = ITEMS.register("portablegenerator_item", PortableGeneratorItem::new);
    public static final RegistryObject<Item> WIRE_ITEM = ITEMS.register("wire_item", WireItem::new);
    public static final RegistryObject<Item> BUS_ITEM = ITEMS.register("bus_item", BusItem::new);
    public static final RegistryObject<Item> EXTERNAL_GRID_ITEM = ITEMS.register("externalgrid_item", ExternalGridItem::new);
    public static final RegistryObject<Item> LAMP_ITEM = ITEMS.register("lamp_item", LampItem::new);
    public static final RegistryObject<Item> LOAD_ITEM = ITEMS.register("load_item", LoadItem::new);
    public static final RegistryObject<Item> MULTIMETER_ITEM = ITEMS.register("multimeter_item", MultimeterItem::new);
    public static final RegistryObject<Item> TRANSFORMER_ITEM = ITEMS.register("transformer_item", TransformerItem::new);

    // Tile Entities
    public static final RegistryObject<TileEntityType<ExternalGridTileEntity>> EXTERNAL_GRID_TILE_ENTITY =
            TILE_ENTITIES.register("externalgrid_tileentity", () -> TileEntityType.Builder.create(ExternalGridTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<LampTileEntity>> LAMP_TILE_ENTITY =
            TILE_ENTITIES.register("lamp_tileentity", () -> TileEntityType.Builder.create(LampTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<GeneratorTileEntity>> GENERATOR_TILE_ENTITY =
            TILE_ENTITIES.register("generator_tileentity", () -> TileEntityType.Builder.create(GeneratorTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<LoadTileEntity>> LOAD_TILE_ENTITY =
            TILE_ENTITIES.register("load_tileentity", () -> TileEntityType.Builder.create(LoadTileEntity::new).build(null));
    public static final RegistryObject<TileEntityType<TransformerTileEntity>> TRANSFORMER_TILE_ENTITY =
            TILE_ENTITIES.register("transformer_tileentity", () -> TileEntityType.Builder.create(TransformerTileEntity::new).build(null));

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
