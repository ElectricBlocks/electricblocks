package edu.uidaho.electricblocks;

import edu.uidaho.electricblocks.blocks.PortableGeneratorBlock;
import edu.uidaho.electricblocks.items.PortableGeneratorItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

    // Registers
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ElectricBlocksMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ElectricBlocksMod.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> PORTABLE_GENERATOR_BLOCK = BLOCKS.register("portablegenerator_block", PortableGeneratorBlock::new);

    // Items
    public static final RegistryObject<Item> PORTABLE_GENERATOR_ITEM = ITEMS.register("portablegenerator_item", PortableGeneratorItem::new);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
