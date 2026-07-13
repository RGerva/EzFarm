/**
 * Generic Class: TreeFarmBlockEntity <T>
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

package com.rgerva.ezfarm.block.entity.machines;

import com.rgerva.ezfarm.block.entity.ModBlockEntities;
import com.rgerva.ezfarm.menu.custom.machines.TreeFarmMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TreeFarmBlockEntity extends BlockEntity implements MenuProvider {
    public TreeFarmBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.TREE_FARM_MACHINE_BE.get(), worldPosition, blockState);
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.ezfarm.tree_farm_machine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NonNull Inventory inventory, @NonNull Player player) {
        return new TreeFarmMenu(i, inventory, this, null, null);
    }

    public void tick(Level level1, BlockPos pos, BlockState state) {
    }
}
