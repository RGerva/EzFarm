/**
 * Generic Class: TreeFarmRecipeCategory <T>
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

package com.rgerva.ezfarm.compat.custom;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.compat.ModJEIRecipeTypes;
import com.rgerva.ezfarm.menu.custom.machines.TreeFarmScreen;
import com.rgerva.ezfarm.recipe.custom.machines.tree.TreeFarmRecipe;

import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TreeFarmRecipeCategory implements IRecipeCategory<RecipeHolder<TreeFarmRecipe>> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(EzFarm.MOD_ID,
            "textures/gui/machines/tree_farm_machine_jei.png");
    private final IDrawable icon;
    private final IDrawable overlay;

    public TreeFarmRecipeCategory(IGuiHelper helper) {
        this.overlay = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TREE_FARM_MACHINE));
    }

    @Override
    public @NonNull IRecipeType<RecipeHolder<TreeFarmRecipe>> getRecipeType() {
        return ModJEIRecipeTypes.TREE_FARM_MACHINE_JEI;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("block.ezfarm.tree_farm_machine");
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 85;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<TreeFarmRecipe> recipe, @NonNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 54, 23).add(recipe.value().input());

        assert Minecraft.getInstance().level != null;
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 54, 46).addItemStacks(Minecraft.getInstance().level
                .registryAccess()
                .lookupOrThrow(Registries.ITEM)
                .getOrThrow(ItemTags.DIRT)
                .stream()
                .map(holder -> new ItemStack(holder.value()))
                .toList());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 34).add(recipe.value().output());
    }

    @Override
    public void draw(@NonNull RecipeHolder<TreeFarmRecipe> recipe, @NonNull IRecipeSlotsView recipeSlotsView, @NonNull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.overlay.draw(guiGraphics, 0, 0);

        int maxMachineEnergy = 64000;
        int minEnergy = recipe.value().min_energy();
        double energyPercent = Math.min(1.0, (double) minEnergy / maxMachineEnergy) * 100;
        int correctYStart = 77 - (int) (energyPercent * 37);

        guiGraphics.fillGradient(152, correctYStart - 10, 168, 77, 0xffb51500, 0xff600b00);

        if (TreeFarmScreen.isMouseAboveArea((int) mouseX, (int) mouseY, 0, 0, 152, 7, 16, 72)) {
            guiGraphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, List.of(Component.literal(minEnergy + " FE/T Needed.")),
                    (int) mouseX, (int) mouseY + 110);
        }
    }
}
