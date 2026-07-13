/**
 * Generic Class: ModBlockEntities <T>
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

package com.rgerva.ezfarm.block.entity;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.block.entity.cables.ModCableBlockEntity;
import com.rgerva.ezfarm.block.entity.generators.ModGeneratorBlockEntity;
import com.rgerva.ezfarm.block.entity.machines.ModMachinesBlockEntity;
import com.rgerva.ezfarm.block.entity.machines.TreeFarmBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, EzFarm.MOD_ID);

    public static final Supplier<BlockEntityType<ModMachinesBlockEntity>> ORE_MACHINE_BE =
            BLOCK_ENTITIES.register("ore_machine_be",
                    () -> new BlockEntityType<>(ModMachinesBlockEntity::new, ModBlocks.EZ_ORE_MACHINE.get()));

    public static final Supplier<BlockEntityType<TreeFarmBlockEntity>> TREE_FARM_MACHINE_BE =
            BLOCK_ENTITIES.register("tree_farm_machine_be",
                    () -> new BlockEntityType<>(TreeFarmBlockEntity::new, ModBlocks.TREE_FARM_MACHINE.get()));

    public static final Supplier<BlockEntityType<ModGeneratorBlockEntity>> CREATIVE_GENERATOR_BE =
            BLOCK_ENTITIES.register("creative_generator_be",
                    () -> new BlockEntityType<>(ModGeneratorBlockEntity::new, ModBlocks.CREATIVE_ENERGY_GENERATOR.get()));

    public static final Supplier<BlockEntityType<ModCableBlockEntity>> COPPER_CABLE_BE =
            BLOCK_ENTITIES.register("cable_copper_be",
                    () -> new BlockEntityType<>(ModCableBlockEntity::new, ModBlocks.COPPER_CABLE.get()));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
