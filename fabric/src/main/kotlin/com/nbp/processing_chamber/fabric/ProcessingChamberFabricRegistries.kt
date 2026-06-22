package com.nbp.processing_chamber.fabric

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.block.ProcessingChamberBlock
import com.nbp.processing_chamber.block.entity.CapsuleBlockEntity
import com.nbp.processing_chamber.block.entity.ModBlockEntities
import com.nbp.processing_chamber.registry.ProcessingChamberBlocks
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import team.reborn.energy.api.EnergyStorage

object ProcessingChamberFabricRegistries {
    val PROCESSING_CHAMBER = Registry.register(
        BuiltInRegistries.BLOCK,
        ProcessingChamberBlocks.PROCESSING_CHAMBER_ID,
        ProcessingChamberBlocks.createProcessingChamberBlock(),
    )

    val PROCESSING_CHAMBER_ITEM = Registry.register(
        BuiltInRegistries.ITEM,
        ProcessingChamberBlocks.PROCESSING_CHAMBER_ID,
        ProcessingChamberBlocks.createProcessingChamberItem(PROCESSING_CHAMBER),
    )

    val CREATIVE_TAB = Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        ResourceLocation(ProcessingChamber.MOD_ID, "main"),
        CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.processing_chamber"))
            .icon { ItemStack(PROCESSING_CHAMBER_ITEM) }
            .displayItems { params, output -> output.accept(PROCESSING_CHAMBER_ITEM) }
            .build(),
    )

    fun init() {
        val capsuleBlockEntityType = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ProcessingChamberBlocks.PROCESSING_CHAMBER_ID,
            BlockEntityType.Builder.of(
                { pos, state -> CapsuleBlockEntity(ModBlockEntities.CAPSULE_BLOCK_ENTITY, pos, state) },
                PROCESSING_CHAMBER,
            ).build(null),
        )
        ModBlockEntities.CAPSULE_BLOCK_ENTITY = capsuleBlockEntityType
        ProcessingChamberBlock.BLOCK_ENTITY_TYPE = capsuleBlockEntityType

        EnergyStorage.SIDED.registerForBlockEntity(
            { be, side ->
                val back = be.blockState.getValue(ProcessingChamberBlock.FACING).opposite
                if (side != back) null
                else object : EnergyStorage {
                    override fun supportsInsertion(): Boolean = true
                    override fun supportsExtraction(): Boolean = false
                    override fun insert(maxAmount: Long, transaction: TransactionContext): Long {
                        val accepted = be.receiveEnergy(maxAmount.toInt(), true)
                        if (accepted > 0) {
                            transaction.addCloseCallback { _, result ->
                                if (result.wasCommitted()) {
                                    be.receiveEnergy(accepted, false)
                                }
                            }
                        }
                        return accepted.toLong()
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
