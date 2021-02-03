package edu.uidaho.electricblocks.items;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class TransformerItem extends BlockItem {

    public TransformerItem() {
        super(RegistryHandler.TRANSFORMER_BLOCK.get(), new Item.Properties()
                .group(ElectricBlocksMod.TAB)
                .maxStackSize(64)
        );
    }
    
}
