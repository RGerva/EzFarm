/**
 * Generic Class: ModGeneratorScreen <T>
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

package com.rgerva.ezfarm.menu.custom.generators;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.menu.custom.renderer.EnergyDisplayTooltipArea;
import com.rgerva.ezfarm.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class ModGeneratorScreen extends AbstractContainerScreen<ModGeneratorMenu> {
    private static final Identifier GUI_TEXTURE =
            Identifier.fromNamespaceAndPath(EzFarm.MOD_ID, "textures/gui/generator/generic_energy_gui.png");
    private EnergyDisplayTooltipArea energyInfoArea;

    public ModGeneratorScreen(ModGeneratorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.inventoryLabelX = 65;
        this.titleLabelX = 20;

        assignEnergyInfoArea();
    }

    private void assignEnergyInfoArea() {
        energyInfoArea = new EnergyDisplayTooltipArea(((width - imageWidth) / 2) + 80,
                ((height - imageHeight) / 2) + 17, menu.blockEntity.getEnergyStorage(null), 16, 52);
    }

    private void renderEnergyAreaTooltip(GuiGraphicsExtractor guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 80, 18, 16, 53)) {
            guiGraphics.setComponentTooltipForNextFrame(this.font, energyInfoArea.getTooltips(), pMouseX, pMouseY);
        }
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int xm, int ym) {
        super.extractLabels(graphics, xm, ym);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltip(graphics, xm, ym, x, y);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        energyInfoArea.render(graphics);
    }

    public static boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return ModUtils.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
