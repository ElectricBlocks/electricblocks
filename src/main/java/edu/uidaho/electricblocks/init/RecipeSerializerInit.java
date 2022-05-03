package edu.uidaho.electricblocks.init;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.recipes.ElecRecipe;
import edu.uidaho.electricblocks.recipes.ElecRecipeSerializer;
import edu.uidaho.electricblocks.recipes.IElecRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerInit {

    public static final IRecipeSerializer<ElecRecipe> ELEC_RECIPE_SERIALIZER = new ElecRecipeSerializer();
    public static final IRecipeType<IElecRecipe> ELEC_TYPE = registerType(IElecRecipe.RECIPE_TYPE_ID);

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<>(
            ForgeRegistries.RECIPE_SERIALIZERS, ElectricBlocksMod.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<?>> ELEC_SERIALIZER = RECIPE_SERIALIZERS.register("elec_furnace",
            () -> ELEC_RECIPE_SERIALIZER);

    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }

    private static <T extends IRecipeType> T registerType(ResourceLocation recipeTypeId) {
        return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
    }
}
