/**
 * Generic Class: ModEvents <T>
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

package com.rgerva.ezfarm.event;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.block.entity.ModBlockEntities;
import com.rgerva.ezfarm.block.entity.cables.ModCableBlockEntity;
import com.rgerva.ezfarm.block.entity.generators.ModGeneratorBlockEntity;
import com.rgerva.ezfarm.block.entity.machines.ModMachinesBlockEntity;
import com.rgerva.ezfarm.block.entity.machines.TreeFarmBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = EzFarm.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlockEntities.ORE_MACHINE_BE.get(), ModMachinesBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModBlockEntities.ORE_MACHINE_BE.get(), ModMachinesBlockEntity::getEnergyStorage);

        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModBlockEntities.CREATIVE_GENERATOR_BE.get(), ModGeneratorBlockEntity::getEnergyStorage);

        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModBlockEntities.COPPER_CABLE_BE.get(), ModCableBlockEntity::getEnergyStorage);

        event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlockEntities.TREE_FARM_MACHINE_BE.get(), TreeFarmBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModBlockEntities.TREE_FARM_MACHINE_BE.get(), TreeFarmBlockEntity::getEnergyStorage);
    }
}
