/**
 * Generic Class: ModMachineMenu <T>
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

package com.rgerva.ezfarm.menu.custom.machines;

import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.block.entity.machines.ModMachinesBlockEntity;
import com.rgerva.ezfarm.menu.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class ModMachineMenu extends AbstractContainerMenu {
    public final ModMachinesBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public ModMachineMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()),
                new ItemStacksResourceHandler(4), new SimpleContainerData(2));
    }

    public ModMachineMenu(int pContainerId, Inventory inv, BlockEntity entity, ItemStacksResourceHandler handler, ContainerData data) {
        super(ModMenuTypes.ORE_MACHINE_MENU.get(), pContainerId);

        blockEntity = ((ModMachinesBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 0, 8, 62));
        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 1, 54, 34));
        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 2, 104, 34) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }
        });
        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 3, 152, 62));

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledArrowProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }


    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.EZ_ORE_MACHINE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
