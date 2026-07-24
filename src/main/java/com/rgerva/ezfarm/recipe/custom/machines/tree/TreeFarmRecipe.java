/**
 * Record: TreeFarmRecipe
 * Immutable data structure for simplified object representation.
 *
 * <p>Created by: superuser
 * <p>On: 2026/jul.
 *
 * <p>GitHub: https://github.com/RGerva
 *
 * <p>Copyright (c) 2026 @RGerva. All Rights Reserved.
 *
 * <p>Licensed under the GNU General Public License, Version 3.0.
 */

package com.rgerva.ezfarm.recipe.custom.machines.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rgerva.ezfarm.recipe.ModRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public record TreeFarmRecipe(Ingredient input, Ingredient dirt, int min_energy,
                             ItemStackTemplate output) implements Recipe<TreeFarmRecipeInput> {

    public static final MapCodec<TreeFarmRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(TreeFarmRecipe::input),
                    Ingredient.CODEC.fieldOf("dirt").forGetter(TreeFarmRecipe::dirt),
                    Codec.INT.fieldOf("min_energy").forGetter(TreeFarmRecipe::min_energy),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(TreeFarmRecipe::output)
            ).apply(instance, TreeFarmRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TreeFarmRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    TreeFarmRecipe::input,

                    Ingredient.CONTENTS_STREAM_CODEC,
                    TreeFarmRecipe::dirt,

                    ByteBufCodecs.INT,
                    TreeFarmRecipe::min_energy,

                    ItemStackTemplate.STREAM_CODEC,
                    TreeFarmRecipe::output,

                    TreeFarmRecipe::new
            );

    @Override
    public boolean matches(@NonNull TreeFarmRecipeInput treeFarmRecipeInput, Level level) {
        if (level.isClientSide()) return false;
        return input.test(treeFarmRecipeInput.getItem(0));
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull TreeFarmRecipeInput treeFarmRecipeInput) {
        return output.create().copy();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public @NonNull String group() {
        return "Machines";
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<TreeFarmRecipeInput>> getSerializer() {
        return ModRecipes.TREE_FARM_MACHINE_SERIALIZER.get();
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<TreeFarmRecipeInput>> getType() {
        return ModRecipes.TREE_FARM_MACHINE_TYPE.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
