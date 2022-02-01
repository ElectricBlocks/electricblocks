package edu.uidaho.electricblocks.items;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ElecFurnaceItem extends BlockItem {

    public ElecFurnaceItem() {
        super(RegistryHandler.ELEC_FURNACE_BLOCK.get(), new Item.Properties()
                .group(ElectricBlocksMod.TAB)
        );
    }

}
