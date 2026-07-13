/**
 * Generic Class: ModJEIRecipeTypes <T>
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

package com.rgerva.ezfarm.compat;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.recipe.custom.machines.ModMachineRecipe;
import com.rgerva.ezfarm.recipe.custom.machines.tree.TreeFarmRecipe;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ModJEIRecipeTypes {
    public static final IRecipeType<RecipeHolder<ModMachineRecipe>> ORE_MACHINE =
            create("ore_machine", ModMachineRecipe.class);

    public static final IRecipeType<RecipeHolder<TreeFarmRecipe>> TREE_FARM_MACHINE_JEI =
            create("tree_farm_machine", TreeFarmRecipe.class);

    public static <R extends Recipe<?>> IRecipeType<RecipeHolder<R>> create(String name, Class<? extends R> recipeClass) {
        Identifier uid = Identifier.fromNamespaceAndPath(EzFarm.MOD_ID, name);
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Class<? extends RecipeHolder<R>> holderClass = (Class<? extends RecipeHolder<R>>) (Object) RecipeHolder.class;
        return IRecipeType.create(uid, holderClass);
    }
}
