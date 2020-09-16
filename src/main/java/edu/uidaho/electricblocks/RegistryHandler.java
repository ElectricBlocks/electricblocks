package edu.uidaho.electricblocks;

import edu.uidaho.electricblocks.blocks.ExternalGridBlock;
import edu.uidaho.electricblocks.blocks.PortableGeneratorBlock;
import edu.uidaho.electricblocks.items.ExternalGridItem;
import edu.uidaho.electricblocks.items.PortableGeneratorItem;
import edu.uidaho.electricblocks.tileentities.ExternalGridTileEntity;
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
    public static final RegistryObject<Block> EXTERNAL_GRID_BLOCK = BLOCKS.register("externalgrid_block", ExternalGridBlock::new);

    // Items
    public static final RegistryObject<Item> PORTABLE_GENERATOR_ITEM = ITEMS.register("portablegenerator_item", PortableGeneratorItem::new);
    public static final RegistryObject<Item> EXTERNAL_GRID_ITEM = ITEMS.register("externalgrid_item", ExternalGridItem::new);

    // Tile Entities
    public static final RegistryObject<TileEntityType<ExternalGridTileEntity>> EXTERNAL_GRID_TILE_ENTITY =
            TILE_ENTITIES.register("externalgrid_tileentity", () -> TileEntityType.Builder.create(ExternalGridTileEntity::new).build(null));

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
