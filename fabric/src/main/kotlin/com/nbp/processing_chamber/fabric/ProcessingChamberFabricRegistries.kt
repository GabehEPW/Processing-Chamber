package com.nbp.processing_chamber.fabric

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.block.ProcessingChamberBlock
import com.nbp.processing_chamber.block.AdvancedProcessingChamberBlock
import com.nbp.processing_chamber.block.entity.CapsuleBlockEntity
import com.nbp.processing_chamber.block.entity.ModBlockEntities
import com.nbp.processing_chamber.config.ProcessingChamberConfig
import com.nbp.processing_chamber.menu.ModMenus
import com.nbp.processing_chamber.menu.ProcessingChamberMenu
import com.nbp.processing_chamber.registry.ProcessingChamberBlocks
import com.nbp.processing_chamber.registry.ProcessingChamberItems
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
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

    val ADVANCED_PROCESSING_CHAMBER = Registry.register(
        BuiltInRegistries.BLOCK,
        ProcessingChamberBlocks.ADVANCED_PROCESSING_CHAMBER_ID,
        ProcessingChamberBlocks.createAdvancedProcessingChamberBlock(),
    )

    val ADVANCED_PROCESSING_CHAMBER_ITEM = Registry.register(
        BuiltInRegistries.ITEM,
        ProcessingChamberBlocks.ADVANCED_PROCESSING_CHAMBER_ID,
        ProcessingChamberBlocks.createAdvancedProcessingChamberItem(ADVANCED_PROCESSING_CHAMBER),
    )

    val PROCESSOR_UPGRADE_KIT = Registry.register(
        BuiltInRegistries.ITEM,
        ProcessingChamberItems.PROCESSOR_UPGRADE_KIT_ID,
        ProcessingChamberItems.createProcessorUpgradeKitItem(),
    )

    val OVERCLOCK_CARD = Registry.register(
        BuiltInRegistries.ITEM,
        ProcessingChamberItems.OVERCLOCK_CARD_ID,
        ProcessingChamberItems.createTooltipItem(ProcessingChamberItems.OVERCLOCK_CARD_NAME),
    )

    val FORTUNE_CARD = Registry.register(
        BuiltInRegistries.ITEM,
        ProcessingChamberItems.FORTUNE_CARD_ID,
        ProcessingChamberItems.createTooltipItem(ProcessingChamberItems.FORTUNE_CARD_NAME),
    )

    val OPTIMIZATION_CARD = Registry.register(
        BuiltInRegistries.ITEM,
        ProcessingChamberItems.OPTIMIZATION_CARD_ID,
        ProcessingChamberItems.createTooltipItem(ProcessingChamberItems.OPTIMIZATION_CARD_NAME),
    )

    val PROCESSING_CHAMBER_MENU: MenuType<ProcessingChamberMenu> = Registry.register(
        BuiltInRegistries.MENU,
        ProcessingChamberBlocks.PROCESSING_CHAMBER_ID,
        MenuType({ containerId, inventory -> ProcessingChamberMenu(containerId, inventory) }, FeatureFlags.DEFAULT_FLAGS),
    )

    val CREATIVE_TAB = Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        ResourceLocation(ProcessingChamber.MOD_ID, "main"),
        CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.processing_chamber"))
            .icon { ItemStack(PROCESSING_CHAMBER_ITEM) }
            .displayItems { params, output ->
                output.accept(PROCESSING_CHAMBER_ITEM)
                output.accept(ADVANCED_PROCESSING_CHAMBER_ITEM)
                output.accept(PROCESSOR_UPGRADE_KIT)
                output.accept(OVERCLOCK_CARD)
                output.accept(FORTUNE_CARD)
                output.accept(OPTIMIZATION_CARD)
            }
            .build(),
    )

    fun init() {
        ModMenus.PROCESSING_CHAMBER_MENU = PROCESSING_CHAMBER_MENU

        val capsuleBlockEntityType = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ProcessingChamberBlocks.PROCESSING_CHAMBER_ID,
            BlockEntityType.Builder.of(
                { pos, state ->
                    val c = ProcessingChamberConfig.normal
                    CapsuleBlockEntity(
                        ModBlockEntities.CAPSULE_BLOCK_ENTITY,
                        pos,
                        state,
                        c.maxEnergy,
                        c.energyPerTick,
                        c.processTime,
                        c.outputAmount,
                        c.upgradeSlots,
                        c.minProcessTime,
                        c.fortuneBonusChance,
                    )
                },
                PROCESSING_CHAMBER,
            ).build(null),
        )
        ModBlockEntities.CAPSULE_BLOCK_ENTITY = capsuleBlockEntityType
        ProcessingChamberBlock.BLOCK_ENTITY_TYPE = capsuleBlockEntityType

        val advancedCapsuleBlockEntityType = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ProcessingChamberBlocks.ADVANCED_PROCESSING_CHAMBER_ID,
            BlockEntityType.Builder.of(
                { pos, state ->
                    val c = ProcessingChamberConfig.advanced
                    CapsuleBlockEntity(
                        ModBlockEntities.ADVANCED_CAPSULE_BLOCK_ENTITY,
                        pos,
                        state,
                        c.maxEnergy,
                        c.energyPerTick,
                        c.processTime,
                        c.outputAmount,
                        c.upgradeSlots,
                        c.minProcessTime,
                        c.fortuneBonusChance,
                    )
                },
                ADVANCED_PROCESSING_CHAMBER,
            ).build(null),
        )
        ModBlockEntities.ADVANCED_CAPSULE_BLOCK_ENTITY = advancedCapsuleBlockEntityType
        AdvancedProcessingChamberBlock.BLOCK_ENTITY_TYPE = advancedCapsuleBlockEntityType

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
                    override fun getCapacity(): Long = be.capacity.toLong()
                }
            },
            ModBlockEntities.CAPSULE_BLOCK_ENTITY,
        )

        EnergyStorage.SIDED.registerForBlockEntity(
            { be, side ->
                val back = be.blockState.getValue(AdvancedProcessingChamberBlock.FACING).opposite
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
                    override fun getCapacity(): Long = be.capacity.toLong()
                }
            },
            ModBlockEntities.ADVANCED_CAPSULE_BLOCK_ENTITY,
        )

        ItemStorage.SIDED.registerForBlockEntity(
            { be, side -> InventoryStorage.of(be, side) },
            ModBlockEntities.CAPSULE_BLOCK_ENTITY,
        )
        ItemStorage.SIDED.registerForBlockEntity(
            { be, side -> InventoryStorage.of(be, side) },
            ModBlockEntities.ADVANCED_CAPSULE_BLOCK_ENTITY,
        )
    }
}
