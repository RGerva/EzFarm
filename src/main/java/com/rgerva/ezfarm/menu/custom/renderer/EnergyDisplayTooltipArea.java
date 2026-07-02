/**
 * Generic Class: EnergyDisplayTooltipArea <T>
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

package com.rgerva.ezfarm.menu.custom.renderer;

import java.util.List;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public class EnergyDisplayTooltipArea {
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private final EnergyHandler energy;

    public EnergyDisplayTooltipArea(int xMin, int yMin, EnergyHandler energy) {
        this(xMin, yMin, energy, 8, 64);
    }

    public EnergyDisplayTooltipArea(int xMin, int yMin, EnergyHandler energy, int width, int height) {
        xPos = xMin;
        yPos = yMin;
        this.width = width;
        this.height = height;
        this.energy = energy;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(energy.getAmountAsInt() + " / " + energy.getCapacityAsInt() + " FE"));
    }

    public void render(GuiGraphicsExtractor guiGraphics) {
        int stored = (int) (height * (energy.getAmountAsInt() / (float) energy.getCapacityAsInt()));
        guiGraphics.fillGradient(xPos, yPos + (height - stored), xPos + width,
                yPos + height, 0xffb51500, 0xff600b00);
    }
}
