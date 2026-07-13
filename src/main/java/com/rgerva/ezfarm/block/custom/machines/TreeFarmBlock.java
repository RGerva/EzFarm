/**
 * Generic Class: TreeFarmBlock <T>
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

package com.rgerva.ezfarm.block.custom.machines;

import com.mojang.serialization.MapCodec;
import com.rgerva.ezfarm.block.entity.ModBlockEntities;
import com.rgerva.ezfarm.block.entity.machines.TreeFarmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TreeFarmBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final MapCodec<TreeFarmBlock> CODEC = simpleCodec(TreeFarmBlock::new);

    public TreeFarmBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new TreeFarmBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, false);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        if (level.isClientSide()) return null;

        return createTickerHelper(type, ModBlockEntities.TREE_FARM_MACHINE_BE.get(), (level1, pos, state, entity) ->
                entity.tick(level1, pos, state));
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof TreeFarmBlockEntity treeFarmBlockEntity) {
                player.openMenu(new SimpleMenuProvider(treeFarmBlockEntity, Component.translatable("block.ezfarm.tree_farm_machine")), pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
}
