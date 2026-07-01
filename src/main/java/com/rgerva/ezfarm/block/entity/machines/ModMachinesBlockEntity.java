/**
 * Generic Class: ModMachinesBlockEntity <T>
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

package com.rgerva.ezfarm.block.entity.machines;

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.block.custom.machines.ModMachinesBlock;
import com.rgerva.ezfarm.block.entity.ModBlockEntities;
import com.rgerva.ezfarm.menu.custom.machines.ModMachineMenu;
import com.rgerva.ezfarm.recipe.ModRecipes;
import com.rgerva.ezfarm.recipe.custom.machines.ModMachineRecipe;
import com.rgerva.ezfarm.recipe.custom.machines.ModMachineRecipeInput;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ModMachinesBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(4) {

        @Override
        protected void onContentsChanged(int index, @NonNull ItemStack previousContents) {
            super.onContentsChanged(index, previousContents);
            ModMachinesBlockEntity.this.setChanged();
        }
    };

    private final ResourceHandler<ItemResource> topHandler = RangedResourceHandler.of(inventory, INPUT_SLOT, INPUT_SLOT + 1);
    private final ResourceHandler<ItemResource> bottomHandler = RangedResourceHandler.of(inventory, OUTPUT_SLOT, OUTPUT_SLOT + 1);

    private final ResourceHandler<ItemResource> frontBackHandler = RangedResourceHandler.of(inventory, INPUT_SLOT, OUTPUT_SLOT + 1);

    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    private static final int INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    public ModMachinesBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.ORE_MACHINE_BE.get(), worldPosition, blockState);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ModMachinesBlockEntity.this.progress;
                    case 1 -> ModMachinesBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0:
                        ModMachinesBlockEntity.this.progress = value;
                    case 1:
                        ModMachinesBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.ezfarm.ore_machine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NonNull Inventory inventory, @NonNull Player player) {
        return new ModMachineMenu(i, inventory, this, this.inventory, this.data);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("ore_machine.progress", progress);
        output.putInt("ore_machine.max_progress", maxProgress);

        output.putChild("inventory", inventory);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr("ore_machine.progress", 0);
        maxProgress = input.getIntOr("ore_machine.max_progress", 72);

        input.child("inventory").ifPresent(inventory::deserialize);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.size());
        for (int i = 0; i < inventory.size(); i++) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(inventory, i);
            inv.setItem(i, new ItemStack(itemAccess.getResource().getItem(), itemAccess.getAmount()));
        }
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public ResourceHandler<ItemResource> getItemHandler(Direction direction) {
        if (direction == null) return inventory;

        Direction facing = this.getBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                ? this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)
                : Direction.NORTH;

        return switch (getRelativeSide(facing, direction)) {
            case DOWN -> topHandler;
            case UP -> bottomHandler;
            case NORTH, SOUTH -> frontBackHandler;
            case WEST, EAST -> null;
        };
    }

    private Direction getRelativeSide(Direction facing, Direction absoluteSide) {
        if (absoluteSide.getAxis().isVertical()) return absoluteSide;

        if (absoluteSide == facing) return Direction.NORTH;
        if (absoluteSide == facing.getOpposite()) return Direction.SOUTH;
        if (absoluteSide == facing.getClockWise()) return Direction.WEST;
        if (absoluteSide == facing.getCounterClockWise()) return Direction.EAST;

        return absoluteSide;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (hasRecipe() && isOutputSlotEmptyOrReceivable()) {
            increaseCraftingProgress();
            setChanged(level, pos, state);
            level.setBlockAndUpdate(pos, state.setValue(ModMachinesBlock.LIT, true));

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
            level.setBlockAndUpdate(pos, state.setValue(ModMachinesBlock.LIT, false));
        }
    }

    private void craftItem() {
        Optional<RecipeHolder<ModMachineRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output().create();
        EzFarm.LOGGER.info("Crafting Item: {}: {}", ModBlocks.EZ_ORE_MACHINE.get(), recipe.get().value());

        try (Transaction transaction = Transaction.openRoot()) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(inventory, OUTPUT_SLOT);

            inventory.extract(inventory.getResource(INPUT_SLOT), 1, transaction);
            inventory.set(OUTPUT_SLOT, ItemResource.of(output), itemAccess.getAmount() + output.getCount());

            transaction.commit();
        }
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<ModMachineRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        ItemStack output = recipe.get().value().assemble(new ModMachineRecipeInput(inventory.getResource(INPUT_SLOT).toStack()));

        boolean outputSlotAmount = canInsertAmountIntoOutputSlot(output.getCount());
        boolean outputSlotItem = canInsertItemIntoOutputSlot(output);

        return outputSlotItem && outputSlotAmount;
    }

    private Optional<RecipeHolder<ModMachineRecipe>> getCurrentRecipe() {
        assert level != null;
        return ((ServerLevel) level).recipeAccess()
                .getRecipeFor(ModRecipes.ORE_MACHINE_TYPE.get(),
                        new ModMachineRecipeInput(inventory.getResource(INPUT_SLOT).toStack()), level);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = inventory.getResource(OUTPUT_SLOT).isEmpty() ? 64 : inventory.getResource(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = inventory.getAmountAsInt(OUTPUT_SLOT);

        return maxCount >= currentCount + count;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return inventory.getResource(OUTPUT_SLOT).isEmpty() ||
                inventory.getResource(OUTPUT_SLOT).is(output.getItem());
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return inventory.getResource(OUTPUT_SLOT).isEmpty() ||
                inventory.getResource(OUTPUT_SLOT).test(stack -> stack.count() < stack.getMaxStackSize());
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
