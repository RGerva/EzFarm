/**
 * Generic Class: ModDatapackProvider <T>
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

import com.rgerva.ezfarm.EzFarm;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();
//            .add(Registries.JUKEBOX_SONG, ModJukeboxSongs::bootstrap)
//            .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap)
//            .add(Registries.VILLAGER_TRADE, ModVillagerTrades::bootstrap)
//            .add(Registries.TRADE_SET, ModTradeSets::bootstrap)
//            .add(Registries.PAINTING_VARIANT, ModPaintings::bootstrap)
//            .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap)

//            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
//            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)

//            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)

//            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
//            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem)

//            .add(Registries.BIOME, ModBiomes::bootstrap);

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(EzFarm.MOD_ID));
    }
}
