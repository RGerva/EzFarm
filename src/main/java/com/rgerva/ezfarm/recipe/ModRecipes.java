/**
 * Generic Class: ModRecipes <T>
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

package com.rgerva.ezfarm.recipe;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.recipe.custom.machines.ModMachineRecipe;
import com.rgerva.ezfarm.recipe.custom.machines.tree.TreeFarmRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, EzFarm.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, EzFarm.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ModMachineRecipe>> ORE_MACHINE_SERIALIZER =
            SERIALIZERS.register("ore_machine", () -> new RecipeSerializer<>(ModMachineRecipe.CODEC, ModMachineRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeType<?>, RecipeType<ModMachineRecipe>> ORE_MACHINE_TYPE =
            TYPES.register("ore_machine", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "Ore Machine";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TreeFarmRecipe>> TREE_FARM_MACHINE_SERIALIZER =
            SERIALIZERS.register("tree_farm_machine", () -> new RecipeSerializer<>(TreeFarmRecipe.CODEC, TreeFarmRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeType<?>, RecipeType<TreeFarmRecipe>> TREE_FARM_MACHINE_TYPE =
            TYPES.register("tree_farm_machine", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "Tree Farm";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
