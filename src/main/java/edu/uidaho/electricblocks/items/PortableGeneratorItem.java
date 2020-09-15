package edu.uidaho.electricblocks.items;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class PortableGeneratorItem extends BlockItem {
    public PortableGeneratorItem() {
        super(RegistryHandler.PORTABLE_GENERATOR_BLOCK.get(), new Item.Properties()
                .group(ElectricBlocksMod.TAB)
                .maxStackSize(64)
        );
    }
}
