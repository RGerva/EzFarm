/**
 * Generic Class: ModMachinesRecipeCategory <T>
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

package com.rgerva.ezfarm.compat.custom;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.compat.ModJEIRecipeTypes;
import com.rgerva.ezfarm.recipe.custom.machines.ModMachineRecipe;


import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ModMachinesRecipeCategory implements IRecipeCategory<RecipeHolder<ModMachineRecipe>> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(EzFarm.MOD_ID,
            "textures/gui/machines/ore_machine_gui.png");
    private final IDrawable icon;
    private final IDrawable overlay;

    public ModMachinesRecipeCategory(IGuiHelper helper) {
        this.overlay = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.EZ_ORE_MACHINE));
    }

    @Override
    public @NonNull IRecipeType<RecipeHolder<ModMachineRecipe>> getRecipeType() {
        return ModJEIRecipeTypes.ORE_MACHINE;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("block.ezfarm.ore_machine");
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ModMachineRecipe> recipe, @NonNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 54, 34).add(recipe.value().inputItem());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 34).add(recipe.value().output());
    }

    @Override
    public void draw(@NonNull RecipeHolder<ModMachineRecipe> recipe, @NonNull IRecipeSlotsView recipeSlotsView, @NonNull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.overlay.draw(guiGraphics, 0, 0);

//        guiGraphics.fillGradient(156, 50, 164, 56, 0xffb51500, 0xff600b00);
//        if (ModMachineScreen.isMouseAboveArea((int) mouseX, (int) mouseY, 0, 0, 156, 11, 8, 48)) {
//            guiGraphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, List.of(Component.literal(25 + "FE/T Needed. Total of 1800 FE.")),
//                    (int) mouseX, (int) mouseY + 110);
//        }
    }
}
