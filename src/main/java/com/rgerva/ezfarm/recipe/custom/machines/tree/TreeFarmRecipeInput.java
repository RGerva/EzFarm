/**
 * Record: TreeFarmRecipeInput
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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

public record TreeFarmRecipeInput(ItemStack input, ItemStack dirt, int min_energy) implements RecipeInput {
    @Override
    public @NonNull ItemStack getItem(int i) {
        return input;
    }

    @Override
    public int size() {
        return 2;
    }
}
