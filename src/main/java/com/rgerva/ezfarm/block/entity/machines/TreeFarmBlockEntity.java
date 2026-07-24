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

import com.rgerva.ezfarm.EzFarm;
import com.rgerva.ezfarm.block.ModBlocks;
import com.rgerva.ezfarm.block.custom.machines.TreeFarmBlock;
import com.rgerva.ezfarm.block.entity.ModBlockEntities;
import com.rgerva.ezfarm.menu.custom.machines.TreeFarmMenu;
import com.rgerva.ezfarm.recipe.ModRecipes;
import com.rgerva.ezfarm.recipe.custom.machines.tree.TreeFarmRecipe;
import com.rgerva.ezfarm.recipe.custom.machines.tree.TreeFarmRecipeInput;

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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TreeFarmBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(3) {
        @Override
        protected void onContentsChanged(int index, @NonNull ItemStack previousContents) {
            super.onContentsChanged(index, previousContents);
            TreeFarmBlockEntity.this.setChanged();
        }
    };

    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1000;

    private static final int DIRT_SLOT = 0;
    private static final int INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    private static final int ENERGY_CRAFT_AMOUNT = 25;      // per tick

    public TreeFarmBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.TREE_FARM_MACHINE_BE.get(), worldPosition, blockState);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TreeFarmBlockEntity.this.progress;
                    case 1 -> TreeFarmBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> TreeFarmBlockEntity.this.progress = i1;
                    case 1 -> TreeFarmBlockEntity.this.maxProgress = i1;
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
        return Component.translatable("block.ezfarm.tree_farm_machine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NonNull Inventory inventory, @NonNull Player player) {
        return new TreeFarmMenu(i, inventory, this, this.inventory, this.data);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("tree_farm_progress", progress);
        output.putInt("tree_farmmax_progress", maxProgress);

        output.putChild("inventory", inventory);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr("tree_farm_progress", 0);
        maxProgress = input.getIntOr("tree_farmmax_progress", 1000);

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
        return null;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (hasRecipe() && hasDirt()) {

            increaseCraftingProgress(10);

            setChanged(level, pos, state);
            level.setBlockAndUpdate(pos, state.setValue(TreeFarmBlock.LIT, true));

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
            level.setBlockAndUpdate(pos, state.setValue(TreeFarmBlock.LIT, false));
        }
    }

    private void craftItem() {
        Optional<RecipeHolder<TreeFarmRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output().create();

        EzFarm.LOGGER.info("Crafting Item: {}: {}", ModBlocks.TREE_FARM_MACHINE.get(), recipe.get().value());

        try (Transaction transaction = Transaction.openRoot()) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(inventory, OUTPUT_SLOT);

            inventory.extract(inventory.getResource(INPUT_SLOT), 1, transaction);
            inventory.set(OUTPUT_SLOT, ItemResource.of(output), itemAccess.getAmount() + output.getCount());

            transaction.commit();
        }
    }

    private Optional<RecipeHolder<TreeFarmRecipe>> getCurrentRecipe() {
        assert level != null;
        return ((ServerLevel) level).recipeAccess().getRecipeFor(ModRecipes.TREE_FARM_MACHINE_TYPE.get(),
                new TreeFarmRecipeInput(inventory.getResource(INPUT_SLOT).toStack(), inventory.getResource(DIRT_SLOT).toStack()), level);
    }

    private boolean hasDirt() {
        Optional<RecipeHolder<TreeFarmRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty() || inventory.getResource(DIRT_SLOT).isEmpty()) return false;

        return inventory.getResource(DIRT_SLOT).isEmpty() ||
                inventory.getResource(DIRT_SLOT).is(recipe.get().value().dirt().getValues());
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<TreeFarmRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        ItemStack output = recipe.get().value().assemble(new TreeFarmRecipeInput(inventory.getResource(INPUT_SLOT).toStack(),
                inventory.getResource(DIRT_SLOT).toStack()));

        boolean outputSlotAmount = canInsertAmountIntoOutputSlot(output.getCount());
        boolean outputSlotItem = canInsertItemIntoOutputSlot(output);

        return outputSlotItem && outputSlotAmount;
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

    private void increaseCraftingProgress(int multiplier) {
        this.progress = this.progress + multiplier;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void resetProgress() {
        this.progress = 0;
    }
}
