/**
 * Generic Class: ModTexturedModel <T>
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

package com.rgerva.ezfarm.datagen.custom;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;

public class ModTexturedModel {

    public static final TextureSlot CABLE = TextureSlot.create("cable");

    public static final TexturedModel.Provider CABLE_CORE = TexturedModel.createDefault(ModTexturedModel::cableCore,
            ModModelTemplates.CABLE_CORE_TEMPLATE);
    public static final TexturedModel.Provider CABLE_SIDE = TexturedModel.createDefault(ModTexturedModel::cableSide,
            ModModelTemplates.CABLE_SIDE_TEMPLATE);

    public static TextureMapping cableCore(Block block) {
        return new TextureMapping().
                put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)).
                put(CABLE, TextureMapping.getBlockTexture(block));
    }

    public static TextureMapping cableSide(Block block) {
        return new TextureMapping().
                put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)).
                put(CABLE, TextureMapping.getBlockTexture(block));
    }
}
