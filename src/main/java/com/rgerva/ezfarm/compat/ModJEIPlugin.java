/**
 * Generic Class: ModJEIPlugin <T>
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
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.compat.custom.ModMachinesRecipeCategory;
import com.rgerva.ezfarm.menu.custom.machines.ModMachineScreen;
import com.rgerva.ezfarm.recipe.ModRecipes;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {

    private static RecipeMap syncedRecipes = RecipeMap.EMPTY;

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(EzFarm.MOD_ID, "jei_plugin");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getRecipes(RecipeMap recipeMap, RecipeType<T> type) {
        return (List) recipeMap.byType(type);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ModMachinesRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ModJEIRecipeTypes.ORE_MACHINE, this.getRecipes(syncedRecipes, ModRecipes.ORE_MACHINE_TYPE.get()));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ModMachineScreen.class, 74, 30, 22, 20,
                ModJEIRecipeTypes.ORE_MACHINE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ModJEIRecipeTypes.ORE_MACHINE, new ItemStack(ModBlocks.EZ_ORE_MACHINE.asItem()));
    }

    @EventBusSubscriber(modid = EzFarm.MOD_ID)
    public static class ServerRecipeSync {
        @SubscribeEvent
        public static void onDatapackSync(OnDatapackSyncEvent event) {
            event.sendRecipes(
                    ModRecipes.ORE_MACHINE_TYPE.get()
            );
        }
    }

    @EventBusSubscriber(modid = EzFarm.MOD_ID, value = Dist.CLIENT)
    public static class ClientRecipeSync {
        @SubscribeEvent
        public static void onRecipeReceived(RecipesReceivedEvent event) {
            syncedRecipes = event.getRecipeMap();
        }
    }
}
