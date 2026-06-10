/**
 * Generic Class: EzFarmDataGenerators <T>
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

import java.util.Collections;
import java.util.List;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = EzFarm.MOD_ID)
public class EzFarmDataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ModModelProvider(packOutput));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ModEntityLootTableProvider::new, LootContextParamSets.ENTITY)
                ), lookupProvider));

        generator.addProvider(true, new ModBlockTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookupProvider));
        generator.addProvider(true, new ModDataMapProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModItemTagProvider(packOutput, lookupProvider));

        generator.addProvider(true, new ModEquipmentAsset(packOutput));
        generator.addProvider(true, new ModGlobalLootModifierProvider(packOutput, lookupProvider));

        generator.addProvider(true, new ModSoundsProvider(packOutput));
        generator.addProvider(true, new ModDatapackProvider(packOutput, lookupProvider));

        generator.addProvider(true, new ModEnchantmentTagProvider(packOutput, lookupProvider));

        generator.addProvider(true, new ModAdvancements(packOutput, lookupProvider));
        generator.addProvider(true, new ModFluidTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModEntityTypeTagProvider(packOutput, lookupProvider));
    }
}
