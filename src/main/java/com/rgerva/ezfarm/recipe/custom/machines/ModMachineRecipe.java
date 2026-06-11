/**
 * Record: ModMachineRecipe
 * Immutable data structure for simplified object representation.
 *
 * <p>Created by: superuser
 * <p>On: 2026/jun.
 *
 * <p>GitHub: https://github.com/RGerva
 *
 * <p>Copyright (c) 2026 @RGerva. All Rights Reserved.
 *
 * <p>Licensed under the GNU General Public License, Version 3.0.
 */

package com.rgerva.ezfarm.recipe.custom.machines;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rgerva.ezfarm.recipe.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public record ModMachineRecipe(Ingredient inputItem,
                               ItemStackTemplate output) implements Recipe<ModMachineRecipeInput> {
    public static final MapCodec<ModMachineRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(ModMachineRecipe::inputItem),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(ModMachineRecipe::output)
            ).apply(instance, ModMachineRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ModMachineRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    ModMachineRecipe::inputItem,

                    ItemStackTemplate.STREAM_CODEC,
                    ModMachineRecipe::output,

                    ModMachineRecipe::new);

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    @Override
    public boolean matches(@NonNull ModMachineRecipeInput modMachineRecipeInput, Level level) {
        if (level.isClientSide()) return false;

        return inputItem.test(modMachineRecipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(ModMachineRecipeInput input) {
        return output.create().copy();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "Machines";
    }

    @Override
    public RecipeSerializer<? extends Recipe<ModMachineRecipeInput>> getSerializer() {
        return ModRecipes.ORE_MACHINE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<ModMachineRecipeInput>> getType() {
        return ModRecipes.ORE_MACHINE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
