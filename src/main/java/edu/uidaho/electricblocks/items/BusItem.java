package edu.uidaho.electricblocks.items;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import net.minecraft.item.BlockItem;

public class BusItem extends BlockItem {

    public BusItem() {
        super(RegistryHandler.BUS_BLOCK.get(), new Properties()
                .group(ElectricBlocksMod.TAB)
                .maxStackSize(64)
        );
    }

}