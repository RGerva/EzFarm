/**
 * Generic Class: TreeFarmMenu <T>
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

package com.rgerva.ezfarm.menu.custom.machines;

import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.block.entity.machines.TreeFarmBlockEntity;
import com.rgerva.ezfarm.menu.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NonNull;

public class TreeFarmMenu extends AbstractContainerMenu {
    public final TreeFarmBlockEntity blockEntity;
    private final Level level;

    public TreeFarmMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()),
                new ItemStacksResourceHandler(0), new SimpleContainerData(0));
    }

    public TreeFarmMenu(int pContainerId, Inventory inv, BlockEntity entity, ItemStacksResourceHandler handler, ContainerData data) {
        super(ModMenuTypes.TREE_FARM_MACHINE_MENU.get(), pContainerId);

        blockEntity = (TreeFarmBlockEntity) entity;
        this.level = inv.player.level();

    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(@NonNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.TREE_FARM_MACHINE.get());
    }
}
