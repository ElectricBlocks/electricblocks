package edu.uidaho.electricblocks.items;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import net.minecraft.item.Item;

public class MultimeterItem extends Item {

    public MultimeterItem() {
        super(new Item.Properties()
            .group(ElectricBlocksMod.TAB)
            .maxStackSize(1)
        );
    }
    
}
