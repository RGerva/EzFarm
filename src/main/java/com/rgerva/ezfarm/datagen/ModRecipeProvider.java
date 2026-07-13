/**
 * Generic Class: ModRecipeProvider <T>
 * A generic structure that works with type parameters.
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

package com.rgerva.ezfarm.datagen;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.recipe.custom.machines.ModMachineRecipeBuilder;
import com.rgerva.ezfarm.recipe.custom.machines.tree.TreeFarmRecipeBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.REDSTONE, ModBlocks.EZ_ORE_MACHINE.get())
                .pattern("ZZZ")
                .pattern("ZZZ")
                .pattern("ZZZ")
                .define('Z', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output);

        customMachine(Items.IRON_ORE, Items.IRON_ORE, 2);
        customMachine(Items.DEEPSLATE_IRON_ORE, Items.DEEPSLATE_IRON_ORE, 2);

        customMachine(Items.COPPER_ORE, Items.COPPER_ORE, 2);
        customMachine(Items.DEEPSLATE_COPPER_ORE, Items.DEEPSLATE_COPPER_ORE, 2);

        customMachine(Items.GOLD_ORE, Items.GOLD_ORE, 2);
        customMachine(Items.DEEPSLATE_GOLD_ORE, Items.DEEPSLATE_GOLD_ORE, 2);

        customMachine(Items.COAL_ORE, Items.COAL_ORE, 2);
        customMachine(Items.DEEPSLATE_COAL_ORE, Items.DEEPSLATE_COAL_ORE, 2);

        shaped(RecipeCategory.REDSTONE, ModBlocks.TREE_FARM_MACHINE.get())
                .pattern("ZZZ")
                .pattern("ZZZ")
                .pattern("ZZZ")
                .define('Z', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output);

        customTreeFarm(Items.OAK_SAPLING, Items.OAK_LOG, 4);
    }

    @Override
    protected <T extends AbstractCookingRecipe> void oreCooking(AbstractCookingRecipe.@NonNull Factory<T> factory, List<ItemLike> smeltables,
                                                                @NonNull RecipeCategory craftingCategory, @NonNull CookingBookCategory cookingCategory,
                                                                @NonNull ItemLike result, float experience, int cookingTime, @NonNull String group, @NonNull String fromDesc) {
        for (ItemLike item : smeltables) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(item), craftingCategory, cookingCategory, result, experience, cookingTime, factory)
                    .group(group)
                    .unlockedBy(getHasName(item), this.has(item))
                    .save(this.output, EzFarm.MOD_ID + ":" + getItemName(result) + fromDesc + "_" + getItemName(item));
        }
    }

    protected void customMachine(ItemLike input, ItemLike result, int count) {
        ModMachineRecipeBuilder.oreMachine(Ingredient.of(input), new ItemStackTemplate(result.asItem(), count))
                .unlockedBy(getHasName(ModBlocks.EZ_ORE_MACHINE.get()), has(input))
                .save(this.output, EzFarm.MOD_ID + ":" + getItemName(ModBlocks.EZ_ORE_MACHINE.get()) + "_duplicate_" + getItemName(input));
    }

    protected void customTreeFarm(ItemLike input, ItemLike result, int count) {
        TreeFarmRecipeBuilder.treeFarmRecipe(Ingredient.of(input), new ItemStackTemplate(result.asItem(), count))
                .unlockedBy(getHasName(ModBlocks.TREE_FARM_MACHINE.get()), has(input))
                .save(this.output, EzFarm.MOD_ID + ":" + getItemName(ModBlocks.TREE_FARM_MACHINE.get()) + "_duplicate_" + getItemName(input));
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public @NonNull String getName() {
            return "EzFarm Recipes";
        }
    }
}
