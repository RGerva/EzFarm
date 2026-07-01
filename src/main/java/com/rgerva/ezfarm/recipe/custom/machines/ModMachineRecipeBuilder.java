/**
 * Generic Class: ModMachineRecipeBuilder <T>
 * A generic structure that works with type parameters.
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

package com.rgerva.ezfarm.recipe.custom.machines;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ModMachineRecipeBuilder implements RecipeBuilder {


    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    private final Ingredient ingredient;
    private final ItemStackTemplate result;

    private ModMachineRecipeBuilder(Ingredient ingredient, ItemStackTemplate result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    public static ModMachineRecipeBuilder oreMachine(Ingredient ingredient, ItemStackTemplate result) {
        return new ModMachineRecipeBuilder(ingredient, result);
    }

    @Override
    public @NonNull RecipeBuilder unlockedBy(@NonNull String name, @NonNull Criterion<?> criterion) {
        advancement.addCriterion(name, criterion);
        return this;
    }

    @Override
    public @NonNull RecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public @NonNull ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(result);
    }

    @Override
    public void save(RecipeOutput output, @NonNull ResourceKey<Recipe<?>> key) {
        output.accept(
                key,
                new ModMachineRecipe(ingredient, result),
                advancement.build(key.identifier())
        );
    }
}
