/**
 * Generic Class: ModCableBlockEntity <T>
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

package com.rgerva.ezfarm.block.entity.cables;

import com.mojang.datafixers.util.Pair;
import com.rgerva.ezfarm.block.entity.ModBlockEntities;

import java.util.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ModCableBlockEntity extends BlockEntity {
    private final int MAX_TRANSFER = 128;
    private boolean loaded;

    private final Map<Pair<BlockPos, Direction>, EnergyHandler> producers = new HashMap<>();
    private final Map<Pair<BlockPos, Direction>, EnergyHandler> consumers = new HashMap<>();
    private final Deque<BlockPos> cableBlocks = new ArrayDeque<>();

    private final SimpleEnergyHandler ENERGY_STORAGE = new SimpleEnergyHandler(128, MAX_TRANSFER) {
        @Override
        protected void onEnergyChanged(int previousAmount) {
            super.onEnergyChanged(previousAmount);
            setChanged();
            assert getLevel() != null;
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    };

    public ModCableBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.COPPER_CABLE_BE.get(), worldPosition, blockState);
    }

    public Map<Pair<BlockPos, Direction>, EnergyHandler> getConsumers() {
        return consumers;
    }

    public Deque<BlockPos> getCableBlocks() {
        return cableBlocks;
    }

    public static Deque<EnergyHandler> getConnectedConsumers(Level level, BlockPos blockPos, Set<BlockPos> checkedCables) {
        Deque<EnergyHandler> consumers = new ArrayDeque<>(1024);

        Deque<BlockPos> cableBlocksLeft = new ArrayDeque<>(1024);
        cableBlocksLeft.add(blockPos);

        checkedCables.add(blockPos);

        while (!cableBlocksLeft.isEmpty()) {
            BlockPos checkPos = cableBlocksLeft.pop();

            BlockEntity blockEntity = level.getBlockEntity(checkPos);
            if (!(blockEntity instanceof ModCableBlockEntity cableBlockEntity))
                continue;

            cableBlockEntity.getCableBlocks().forEach(pos -> {
                if (checkedCables.add(pos)) {
                    cableBlocksLeft.add(pos);
                }
            });

            consumers.addAll(cableBlockEntity.getConsumers().values());
        }

        return consumers;
    }

    public static void updateConnections(@NonNull Level level, @NonNull BlockPos pos, BlockState newState, ModCableBlockEntity blockEntity) {
        if (level.isClientSide()) return;

        blockEntity.producers.clear();
        blockEntity.consumers.clear();
        blockEntity.cableBlocks.clear();

        for (Direction direction : Direction.values()) {
            BlockPos testPos = pos.relative(direction);

            BlockEntity testBlockEntity = level.getBlockEntity(testPos);

            if (testBlockEntity instanceof ModCableBlockEntity cableBlockEntity) {
                blockEntity.cableBlocks.add(testPos);
                continue;
            }

            EnergyHandler energyStorage = level.getCapability(Capabilities.Energy.BLOCK, testPos,
                    level.getBlockState(testPos), testBlockEntity, direction.getOpposite());
            if (energyStorage == null)
                continue;

            blockEntity.producers.put(Pair.of(testPos, direction.getOpposite()), energyStorage);
            blockEntity.consumers.put(Pair.of(testPos, direction.getOpposite()), energyStorage);
        }
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState, ModCableBlockEntity entity) {
        if (level.isClientSide())
            return;

        if (!entity.loaded) {
            updateConnections(level, blockPos, blockState, entity);

            entity.loaded = true;
        }

        List<EnergyHandler> energyProduction = new ArrayList<>();
        List<Integer> energyProductionValues = new ArrayList<>();

        //Prioritize stored energy for PUSH mode
        int productionSum = entity.ENERGY_STORAGE.getAmountAsInt(); //Will always be 0 if in PULL only mode
        for (EnergyHandler energyStorage : entity.producers.values()) {
            try (Transaction transaction = Transaction.open(null)) {
                int extracted = energyStorage.extract(MAX_TRANSFER, transaction);

                if (extracted <= 0)
                    continue;

                energyProduction.add(energyStorage);
                energyProductionValues.add(extracted);
                productionSum += extracted;
            }
        }

        if (productionSum <= 0)
            return;

        List<EnergyHandler> energyConsumption = new ArrayList<>();
        List<Integer> energyConsumptionValues = new ArrayList<>();

        Deque<EnergyHandler> consumers = getConnectedConsumers(level, blockPos, new HashSet<>());

        int consumptionSum = 0;
        for (EnergyHandler energyStorage : consumers) {
            try (Transaction transaction = Transaction.open(null)) {
                int received = energyStorage.insert(MAX_TRANSFER, transaction);

                if (received <= 0)
                    continue;

                energyConsumption.add(energyStorage);
                energyConsumptionValues.add(received);
                consumptionSum += received;
            }
        }

        if (consumptionSum <= 0)
            return;

        int transferLeft = Math.min(Math.min(MAX_TRANSFER, productionSum), consumptionSum);

        int extractInternally = 0;
        extractInternally = Math.min(entity.ENERGY_STORAGE.getAmountAsInt(), transferLeft);
        try (Transaction transaction = Transaction.open(null)) {
            entity.ENERGY_STORAGE.extract(extractInternally, transaction);

            transaction.commit();
        }

        List<Integer> energyProductionDistributed = new ArrayList<>();
        for (int i = 0; i < energyProduction.size(); i++)
            energyProductionDistributed.add(0);

        //Set to 0 for PUSH only mode
        int productionLeft = transferLeft - extractInternally;
        int divisor = energyProduction.size();
        outer:
        while (productionLeft > 0) {
            int productionPerProducer = productionLeft / divisor;
            if (productionPerProducer == 0) {
                divisor = Math.max(1, divisor - 1);
                productionPerProducer = productionLeft / divisor;
            }

            for (int i = 0; i < energyProductionValues.size(); i++) {
                int productionDistributed = energyProductionDistributed.get(i);
                int productionOfProducerLeft = energyProductionValues.get(i) - productionDistributed;

                int productionDistributedNew = Math.min(productionPerProducer, Math.min(productionOfProducerLeft, productionLeft));
                energyProductionDistributed.set(i, productionDistributed + productionDistributedNew);
                productionLeft -= productionDistributedNew;
                if (productionLeft == 0)
                    break outer;
            }
        }

        for (int i = 0; i < energyProduction.size(); i++) {
            int energy = energyProductionDistributed.get(i);
            if (energy > 0)
                try (Transaction transaction = Transaction.open(null)) {
                    energyProduction.get(i).extract(energy, transaction);
                    transaction.commit();
                }
        }

        List<Integer> energyConsumptionDistributed = new ArrayList<>();
        for (int i = 0; i < energyConsumption.size(); i++)
            energyConsumptionDistributed.add(0);

        int consumptionLeft = transferLeft;
        divisor = energyConsumption.size();
        outer:
        while (consumptionLeft > 0) {
            int consumptionPerConsumer = consumptionLeft / divisor;
            if (consumptionPerConsumer == 0) {
                divisor = Math.max(1, divisor - 1);
                consumptionPerConsumer = consumptionLeft / divisor;
            }

            for (int i = 0; i < energyConsumptionValues.size(); i++) {
                int consumptionDistributed = energyConsumptionDistributed.get(i);
                int consumptionOfConsumerLeft = energyConsumptionValues.get(i) - consumptionDistributed;

                int consumptionDistributedNew = Math.min(consumptionOfConsumerLeft, Math.min(consumptionPerConsumer, consumptionLeft));
                energyConsumptionDistributed.set(i, consumptionDistributed + consumptionDistributedNew);
                consumptionLeft -= consumptionDistributedNew;
                if (consumptionLeft == 0)
                    break outer;
            }
        }

        for (int i = 0; i < energyConsumption.size(); i++) {
            int energy = energyConsumptionDistributed.get(i);
            if (energy > 0)
                try (Transaction transaction = Transaction.open(null)) {
                    energyConsumption.get(i).insert(energy, transaction);
                    transaction.commit();
                }
        }
    }

    private void fillUpOnEnergy() {
        try (Transaction transaction = Transaction.openRoot()) {
            this.ENERGY_STORAGE.insert(160, transaction);
            transaction.commit();
        }
    }

    public EnergyHandler getEnergyStorage(@Nullable Direction direction) {
        return this.ENERGY_STORAGE;
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);

        ENERGY_STORAGE.deserialize(input);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);

        ENERGY_STORAGE.serialize(output);
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
