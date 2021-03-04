package edu.uidaho.electricblocks.items;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class LoadItem extends BlockItem {
    
    public LoadItem() {
        super(RegistryHandler.LOAD_BLOCK.get(), new Item.Properties()
            .group(ElectricBlocksMod.TAB)
        );
    }

}