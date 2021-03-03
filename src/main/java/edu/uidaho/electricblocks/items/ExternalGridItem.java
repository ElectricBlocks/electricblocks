package edu.uidaho.electricblocks.items;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ExternalGridItem extends BlockItem {

    public ExternalGridItem() {
        super(RegistryHandler.EXTERNAL_GRID_BLOCK.get(), new Item.Properties()
                .group(ElectricBlocksMod.TAB)
        );
    }

}