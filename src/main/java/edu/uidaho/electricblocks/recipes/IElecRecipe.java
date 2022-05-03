package edu.uidaho.electricblocks.recipes;

import javax.annotation.Nonnull;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface IElecRecipe extends IRecipe<RecipeWrapper> {

    ResourceLocation RECIPE_TYPE_ID = new ResourceLocation(ElectricBlocksMod.MOD_ID, "elec_furnace");

    @Nonnull
    @Override
    default IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getValue(RECIPE_TYPE_ID).get();
    }

    @Override
    default boolean canFit(int width, int height) {
        return false;
    }

    Ingredient getInput();
}
