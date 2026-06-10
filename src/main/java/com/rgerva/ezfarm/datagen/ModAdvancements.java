/**
 * Generic Class: ModAdvancements <T>
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

package com.rgerva.ezfarm.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;

public class ModAdvancements extends AdvancementProvider {
    public ModAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new EzFarmAdvancements()));
    }

    public static class EzFarmAdvancements implements AdvancementSubProvider {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {

        }
    }
}
