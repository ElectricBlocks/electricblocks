package edu.uidaho.electricblocks.recipes;

import edu.uidaho.electricblocks.init.RecipeSerializerInit;
import edu.uidaho.electricblocks.recipes.ElecRecipeSerializer;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ElecRecipe implements IElecRecipe{
    private final ResourceLocation id;
    private Ingredient input;
    private final ItemStack output;

    public ElecRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        if (this.input.test(inv.getStackInSlot(0))) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.ELEC_SERIALIZER.get();
    }

    @Override
    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(null, this.input);
    }
}
