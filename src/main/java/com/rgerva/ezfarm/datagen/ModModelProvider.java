/**
 * Generic Class: ModModelProvider <T>
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
import com.rgerva.ezfarm.block.custom.cables.ModCableBlock;
import com.rgerva.ezfarm.datagen.custom.ModTexturedModel;
import com.rgerva.ezfarm.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, EzFarm.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.DUMMY.get(), ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialCube(ModBlocks.CREATIVE_ENERGY_GENERATOR.get());

        blockModels.createFurnace(ModBlocks.EZ_ORE_MACHINE.get(), TexturedModel.ORIENTABLE);
        blockModels.createFurnace(ModBlocks.TREE_FARM_MACHINE.get(), TexturedModel.ORIENTABLE);

        createCable(ModBlocks.COPPER_CABLE, blockModels);
    }

    protected static void createCable(Holder<Block> block, BlockModelGenerators blockModels) {

        Identifier cableCore = ModTexturedModel.CABLE_CORE.get(block.value()).create(block.value(), blockModels.modelOutput);
        Identifier cableSide = ModTexturedModel.CABLE_SIDE.get(block.value()).createWithSuffix(block.value(), "_side", blockModels.modelOutput);

        blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block.value()).
                with(
                        new MultiVariant(WeightedList.of(new Variant(cableCore)))).
                with(
                        new ConditionBuilder().
                                term(ModCableBlock.UP, true),
                        new MultiVariant(WeightedList.of(new Variant(cableSide))).
                                with(BlockModelGenerators.X_ROT_270)).
                with(
                        new ConditionBuilder().
                                term(ModCableBlock.DOWN, true),
                        new MultiVariant(WeightedList.of(new Variant(cableSide))).
                                with(BlockModelGenerators.X_ROT_90)).
                with(
                        new ConditionBuilder().
                                term(ModCableBlock.NORTH, true),
                        new MultiVariant(WeightedList.of(new Variant(cableSide)))).
                with(
                        new ConditionBuilder().
                                term(ModCableBlock.SOUTH, true),
                        new MultiVariant(WeightedList.of(new Variant(cableSide))).
                                with(BlockModelGenerators.Y_ROT_180)).
                with(
                        new ConditionBuilder().
                                term(ModCableBlock.EAST, true),
                        new MultiVariant(WeightedList.of(new Variant(cableSide))).
                                with(BlockModelGenerators.Y_ROT_90)).
                with(
                        new ConditionBuilder().
                                term(ModCableBlock.WEST, true),
                        new MultiVariant(WeightedList.of(new Variant(cableSide))).
                                with(BlockModelGenerators.Y_ROT_270))
        );

        blockModels.registerSimpleItemModel(block.value(), cableCore);
    }
}
