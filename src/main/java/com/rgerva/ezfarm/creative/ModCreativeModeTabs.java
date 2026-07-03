/**
 * Generic Class: ModCreativeModeTabs <T>
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

package com.rgerva.ezfarm.creative;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.item.ModItems;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EzFarm.MOD_ID);


    public static final Supplier<CreativeModeTab> EZFARM_ITEMS_TAB = CREATIVE_MODE_TABS.register("tab.ezfarm",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CREATIVE_ENERGY_GENERATOR.get()))
                    .title(Component.translatable("itemGroup.ezfarm"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.DUMMY.get());
                        output.accept(ModBlocks.EZ_ORE_MACHINE.get());
                        output.accept(ModBlocks.CREATIVE_ENERGY_GENERATOR.get());
                        output.accept(ModBlocks.COPPER_CABLE.get());
                    }).build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.EZ_ORE_MACHINE.get());
        }

        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            event.accept(ModBlocks.CREATIVE_ENERGY_GENERATOR.get());
        }
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
