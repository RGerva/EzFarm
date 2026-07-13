/**
 * Generic Class: ModBlockTagProvider <T>
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
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.tag.ModTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, EzFarm.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.getRK(ModBlocks.EZ_ORE_MACHINE.get()))
                .add(ModBlocks.getRK(ModBlocks.COPPER_CABLE.get()));

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.getRK(ModBlocks.TREE_FARM_MACHINE.get()));

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.getRK(ModBlocks.COPPER_CABLE.get()));

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.getRK(ModBlocks.EZ_ORE_MACHINE.get()))
                .add(ModBlocks.getRK(ModBlocks.TREE_FARM_MACHINE.get()));

        tag(ModTags.Blocks.EZFARM_CREATIVE)
                .add(ModBlocks.getRK(ModBlocks.CREATIVE_ENERGY_GENERATOR.get()));

        tag(ModTags.Blocks.EZFARM_MACHINES)
                .add(ModBlocks.getRK(ModBlocks.EZ_ORE_MACHINE.get()))
                .add(ModBlocks.getRK(ModBlocks.TREE_FARM_MACHINE.get()));

        tag(ModTags.Blocks.EZFARM_CABLES)
                .add(ModBlocks.getRK(ModBlocks.COPPER_CABLE.get()));

        tag(ModTags.Blocks.EZFARM_GENERATORS)
                .add(ModBlocks.getRK(ModBlocks.CREATIVE_ENERGY_GENERATOR.get()));

        tag(ModTags.Blocks.EZFARM_ENERGY)
                .add(ModBlocks.getRK(ModBlocks.CREATIVE_ENERGY_GENERATOR.get()))
                .add(ModBlocks.getRK(ModBlocks.COPPER_CABLE.get()))
                .add(ModBlocks.getRK(ModBlocks.EZ_ORE_MACHINE.get()))
                .add(ModBlocks.getRK(ModBlocks.TREE_FARM_MACHINE.get()));
    }
}
