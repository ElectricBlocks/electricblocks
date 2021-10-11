package edu.uidaho.electricblocks.items;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.RegistryHandler;
import net.minecraft.item.BlockItem;

public class BatteryItem extends BlockItem {

    public BatteryItem() {
        super(RegistryHandler.BATTERY_BLOCK.get(), new Properties()
                .group(ElectricBlocksMod.TAB)
                .maxStackSize(64)
        );
    }

}