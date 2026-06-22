package com.nbp.apricorn_chamber.fabric

import com.nbp.apricorn_chamber.block.ApricornChamberBlock
import com.nbp.apricorn_chamber.block.entity.CapsuleBlockEntity
import com.nbp.apricorn_chamber.block.entity.ModBlockEntities
import com.nbp.apricorn_chamber.registry.ApricornChamberBlocks
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.level.block.entity.BlockEntityType
import team.reborn.energy.api.EnergyStorage

object ApricornChamberFabricRegistries {
    val APRICORN_CHAMBER = Registry.register(
        BuiltInRegistries.BLOCK,
        ApricornChamberBlocks.APRICORN_CHAMBER_ID,
        ApricornChamberBlocks.createApricornChamberBlock(),
    )

    val APRICORN_CHAMBER_ITEM = Registry.register(
        BuiltInRegistries.ITEM,
        ApricornChamberBlocks.APRICORN_CHAMBER_ID,
        ApricornChamberBlocks.createApricornChamberItem(APRICORN_CHAMBER),
    )

    fun init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register { entries ->
            entries.accept(APRICORN_CHAMBER_ITEM)
        }

        val capsuleBlockEntityType = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ApricornChamberBlocks.APRICORN_CHAMBER_ID,
            BlockEntityType.Builder.of(
                { pos, state -> CapsuleBlockEntity(ModBlockEntities.CAPSULE_BLOCK_ENTITY, pos, state) },
                APRICORN_CHAMBER,
            ).build(null),
        )
        ModBlockEntities.CAPSULE_BLOCK_ENTITY = capsuleBlockEntityType
        ApricornChamberBlock.BLOCK_ENTITY_TYPE = capsuleBlockEntityType

        EnergyStorage.SIDED.registerForBlockEntity(
            { be, _ ->
                object : EnergyStorage {
                    override fun supportsInsertion(): Boolean = true
                    override fun supportsExtraction(): Boolean = false
                    override fun insert(maxAmount: Long, transaction: TransactionContext): Long {
                        val canAccept = minOf(maxAmount, (CapsuleBlockEntity.CAPACITY - be.energy).toLong())
                            .coerceAtLeast(0)
                        if (canAccept > 0) {
                            be.receiveEnergy(canAccept.toInt(), false)
                        }
                        return canAccept
                    }
                    override fun extract(maxAmount: Long, transaction: TransactionContext): Long = 0
                    override fun getAmount(): Long = be.energy.toLong()
                    override fun getCapacity(): Long = CapsuleBlockEntity.CAPACITY.toLong()
                }
            },
            ModBlockEntities.CAPSULE_BLOCK_ENTITY,
        )
    }
}
