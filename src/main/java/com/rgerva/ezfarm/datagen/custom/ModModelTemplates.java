/**
 * Generic Class: ModModelTemplates <T>
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

import static com.rgerva.ezfarm.EzFarm.MOD_ID;

import java.util.Optional;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;

public class ModModelTemplates {

    public static final ModelTemplate CABLE_CORE_TEMPLATE = block("cable_core_template"
    );
    public static final ModelTemplate CABLE_SIDE_TEMPLATE = block("cable_side_template"
    );

    private static ModelTemplate block(String parent) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(MOD_ID, "block/" + parent)), Optional.empty(), TextureSlot.PARTICLE, ModTexturedModel.CABLE);
    }

    private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureSlots) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(MOD_ID, "block/" + parent)), Optional.of(variant), requiredTextureSlots);
    }
}
