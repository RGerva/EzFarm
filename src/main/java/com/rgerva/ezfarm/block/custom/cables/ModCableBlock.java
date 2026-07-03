/**
 * Generic Class: ModCableBlock <T>
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

package com.rgerva.ezfarm.block.custom.cables;

import com.mojang.serialization.MapCodec;
import com.rgerva.ezfarm.block.entity.ModBlockEntities;
import com.rgerva.ezfarm.block.entity.cables.ModCableBlockEntity;
import com.rgerva.ezfarm.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ModCableBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<ModCableBlock> CODEC = simpleCodec(ModCableBlock::new);

    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_CORE = Block.box(6.d, 6.d, 6.d, 10.d, 10.d, 10.d);
    private static final VoxelShape SHAPE_UP = Block.box(6.d, 10.d, 6.d, 10.d, 16.d, 10.d);
    private static final VoxelShape SHAPE_DOWN = Block.box(6.d, 0.d, 6.d, 10.d, 6.d, 10.d);
    private static final VoxelShape SHAPE_NORTH = Block.box(6.d, 6.d, 0.d, 10.d, 10.d, 6.d);
    private static final VoxelShape SHAPE_SOUTH = Block.box(6.d, 6.d, 10.d, 10.d, 10.d, 16.d);
    private static final VoxelShape SHAPE_EAST = Block.box(10.d, 6.d, 6.d, 16.d, 10.d, 10.d);
    private static final VoxelShape SHAPE_WEST = Block.box(0.d, 6.d, 6.d, 6.d, 10.d, 10.d);

    public ModCableBlock(Properties properties) {
        super(properties.pushReaction(PushReaction.BLOCK));

        this.registerDefaultState(this.stateDefinition.any().setValue(UP, false).setValue(DOWN, false).
                setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false).
                setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(UP).add(DOWN).add(NORTH).add(SOUTH).add(EAST).add(WEST).add(WATERLOGGED);
    }

    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new ModCableBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NonNull RenderShape getRenderShape(@NonNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, ModBlockEntities.COPPER_CABLE_BE.get(), (((level1, blockPos, blockState1, entity) ->
                entity.tick(level1, blockPos, blockState1, entity))));
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        VoxelShape shape = SHAPE_CORE;

        if (state.getValue(UP)) {
            shape = Shapes.or(shape, SHAPE_UP);
        } else if (state.getValue(DOWN)) {
            shape = Shapes.or(shape, SHAPE_DOWN);
        } else if (state.getValue(NORTH)) {
            shape = Shapes.or(shape, SHAPE_NORTH);
        } else if (state.getValue(SOUTH)) {
            shape = Shapes.or(shape, SHAPE_SOUTH);
        } else if (state.getValue(EAST)) {
            shape = Shapes.or(shape, SHAPE_EAST);
        } else if (state.getValue(WEST)) {
            shape = Shapes.or(shape, SHAPE_WEST);
        }
        return shape;
    }

    @Override
    protected @NonNull BlockState mirror(@NonNull BlockState state, @NonNull Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.
                    setValue(NORTH, state.getValue(SOUTH)).
                    setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.
                    setValue(EAST, state.getValue(WEST)).
                    setValue(WEST, state.getValue(EAST));
            default -> state;
        };
    }

    @Override
    protected @NonNull BlockState rotate(@NonNull BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> state.
                    setValue(NORTH, state.getValue(WEST)).
                    setValue(SOUTH, state.getValue(EAST)).
                    setValue(EAST, state.getValue(NORTH)).
                    setValue(WEST, state.getValue(SOUTH));
            case CLOCKWISE_180 -> state.
                    setValue(NORTH, state.getValue(SOUTH)).
                    setValue(SOUTH, state.getValue(NORTH)).
                    setValue(EAST, state.getValue(WEST)).
                    setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> state.
                    setValue(NORTH, state.getValue(EAST)).
                    setValue(SOUTH, state.getValue(WEST)).
                    setValue(EAST, state.getValue(SOUTH)).
                    setValue(WEST, state.getValue(NORTH));
            default -> state;
        };
    }

    @Override
    protected @NonNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected @NonNull BlockState updateShape(BlockState state, @NonNull LevelReader level, @NonNull ScheduledTickAccess ticks, @NonNull BlockPos pos,
                                              @NonNull Direction directionToNeighbour, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        if (state.getValue(WATERLOGGED))
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    private boolean shouldConnectTo(Level level, BlockPos selfPos, Direction direction) {
        BlockPos toPos = selfPos.relative(direction);
        BlockEntity blockEntity = level.getBlockEntity(toPos);

        EnergyHandler energyStorage = level.getCapability(Capabilities.Energy.BLOCK,
                toPos, level.getBlockState(toPos), blockEntity, direction.getOpposite());
        return energyStorage != null;
    }

    @Override
    protected void neighborChanged(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);

        if (level.isClientSide()) return;

        FluidState fluidState = level.getFluidState(pos);

        BlockState newState = defaultBlockState().
                setValue(UP, shouldConnectTo(level, pos, Direction.UP)).
                setValue(DOWN, shouldConnectTo(level, pos, Direction.DOWN)).
                setValue(NORTH, shouldConnectTo(level, pos, Direction.NORTH)).
                setValue(SOUTH, shouldConnectTo(level, pos, Direction.SOUTH)).
                setValue(EAST, shouldConnectTo(level, pos, Direction.EAST)).
                setValue(WEST, shouldConnectTo(level, pos, Direction.WEST)).
                setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);

        level.setBlockAndUpdate(pos, newState);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ModCableBlockEntity))
            return;

        ModCableBlockEntity.updateConnections(level, pos, newState, (ModCableBlockEntity) blockEntity);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos selfPos = context.getClickedPos();
        FluidState fluidState = level.getFluidState(selfPos);

        return defaultBlockState().
                setValue(UP, shouldConnectTo(level, selfPos, Direction.UP)).
                setValue(DOWN, shouldConnectTo(level, selfPos, Direction.DOWN)).
                setValue(NORTH, shouldConnectTo(level, selfPos, Direction.NORTH)).
                setValue(SOUTH, shouldConnectTo(level, selfPos, Direction.SOUTH)).
                setValue(EAST, shouldConnectTo(level, selfPos, Direction.EAST)).
                setValue(WEST, shouldConnectTo(level, selfPos, Direction.WEST)).
                setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }
}
