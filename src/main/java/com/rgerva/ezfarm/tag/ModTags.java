/**
 * Generic Class: ModTags <T>
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

package com.rgerva.ezfarm.tag;

import com.rgerva.ezfarm.EzFarm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> EZFARM_CREATIVE = createTag("ez_farm_creative");
        public static final TagKey<Block> EZFARM_MACHINES = createTag("ez_farm_machines");
        public static final TagKey<Block> EZFARM_CABLES = createTag("ez_farm_cables");
        public static final TagKey<Block> EZFARM_GENERATORS = createTag("ez_farm_generator");
        public static final TagKey<Block> EZFARM_BLOCK_ENERGY = createTag("ez_farm_block_energy");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath(EzFarm.MOD_ID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> EZFARM_ITEM_ENERGY = createTag("ez_farm_item_energy");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(EzFarm.MOD_ID, name));
        }
    }

    public static class Trades {

        private static TagKey<VillagerTrade> createTag(String name) {
            return TagKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(EzFarm.MOD_ID, name));
        }
    }
}
