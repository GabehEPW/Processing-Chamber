package com.nbp.processing_chamber.neoforge

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.block.ProcessingChamberBlock
import com.nbp.processing_chamber.block.AdvancedProcessingChamberBlock
import com.nbp.processing_chamber.block.entity.CapsuleBlockEntity
import com.nbp.processing_chamber.block.entity.ModBlockEntities
import com.nbp.processing_chamber.config.ProcessingChamberConfig
import com.nbp.processing_chamber.registry.ProcessingChamberBlocks
import com.nbp.processing_chamber.registry.ProcessingChamberItems
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ProcessingChamberNeoForgeRegistries {
    private val BLOCKS: DeferredRegister<Block> =
        DeferredRegister.create(Registries.BLOCK, ProcessingChamber.MOD_ID)

    private val ITEMS: DeferredRegister<Item> =
        DeferredRegister.create(Registries.ITEM, ProcessingChamber.MOD_ID)

    private val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ProcessingChamber.MOD_ID)

    private val CREATIVE_TABS: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProcessingChamber.MOD_ID)

    val CREATIVE_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        CREATIVE_TABS.register("main", Supplier {
            CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemGroup.processing_chamber"))
                .icon { ItemStack(PROCESSING_CHAMBER_ITEM.get()) }
                .displayItems { params, output ->
                    output.accept(PROCESSING_CHAMBER_ITEM.get())
                    output.accept(ADVANCED_PROCESSING_CHAMBER_ITEM.get())
                    output.accept(UPGRADE_PROCESSING_CHAMBER.get())
                }
                .build()
        })

    val PROCESSING_CHAMBER: DeferredHolder<Block, Block> =
        BLOCKS.register(ProcessingChamberBlocks.PROCESSING_CHAMBER_NAME, Supplier {
            ProcessingChamberBlocks.createProcessingChamberBlock()
        })

    val PROCESSING_CHAMBER_ITEM: DeferredHolder<Item, BlockItem> =
        ITEMS.register(ProcessingChamberBlocks.PROCESSING_CHAMBER_NAME, Supplier {
            BlockItem(PROCESSING_CHAMBER.get(), Item.Properties())
        })

    val ADVANCED_PROCESSING_CHAMBER: DeferredHolder<Block, Block> =
        BLOCKS.register(ProcessingChamberBlocks.ADVANCED_PROCESSING_CHAMBER_NAME, Supplier {
            ProcessingChamberBlocks.createAdvancedProcessingChamberBlock()
        })

    val ADVANCED_PROCESSING_CHAMBER_ITEM: DeferredHolder<Item, BlockItem> =
        ITEMS.register(ProcessingChamberBlocks.ADVANCED_PROCESSING_CHAMBER_NAME, Supplier {
            BlockItem(ADVANCED_PROCESSING_CHAMBER.get(), Item.Properties())
        })

    val UPGRADE_PROCESSING_CHAMBER: DeferredHolder<Item, Item> =
        ITEMS.register(ProcessingChamberItems.UPGRADE_PROCESSING_CHAMBER_NAME, Supplier {
            ProcessingChamberItems.createUpgradeItem()
        })

    private val CAPSULE_BE: DeferredHolder<BlockEntityType<*>, BlockEntityType<CapsuleBlockEntity>> =
        BLOCK_ENTITIES.register(ProcessingChamberBlocks.PROCESSING_CHAMBER_NAME, Supplier {
            val c = ProcessingChamberConfig.normal
            val type = BlockEntityType.Builder.of(
                { pos, state ->
                    CapsuleBlockEntity(ModBlockEntities.CAPSULE_BLOCK_ENTITY, pos, state, c.energyPerTick, c.processTime, c.outputAmount)
                },
                PROCESSING_CHAMBER.get(),
            ).build(null)
            ModBlockEntities.CAPSULE_BLOCK_ENTITY = type
            ProcessingChamberBlock.BLOCK_ENTITY_TYPE = type
            type
        })

    private val ADVANCED_CAPSULE_BE: DeferredHolder<BlockEntityType<*>, BlockEntityType<CapsuleBlockEntity>> =
        BLOCK_ENTITIES.register(ProcessingChamberBlocks.ADVANCED_PROCESSING_CHAMBER_NAME, Supplier {
            val c = ProcessingChamberConfig.advanced
            val type = BlockEntityType.Builder.of(
                { pos, state ->
                    CapsuleBlockEntity(ModBlockEntities.ADVANCED_CAPSULE_BLOCK_ENTITY, pos, state, c.energyPerTick, c.processTime, c.outputAmount)
                },
                ADVANCED_PROCESSING_CHAMBER.get(),
            ).build(null)
            ModBlockEntities.ADVANCED_CAPSULE_BLOCK_ENTITY = type
            AdvancedProcessingChamberBlock.BLOCK_ENTITY_TYPE = type
            type
        })

    fun register(modEventBus: IEventBus) {
        BLOCKS.register(modEventBus)
        ITEMS.register(modEventBus)
        BLOCK_ENTITIES.register(modEventBus)
        CREATIVE_TABS.register(modEventBus)

        modEventBus.addListener<RegisterCapabilitiesEvent> { event ->
            event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.BLOCK,
                CAPSULE_BE.get(),
                { be, side ->
                    val back = be.blockState.getValue(ProcessingChamberBlock.FACING).opposite
                    if (side != back) null
                    else object : IEnergyStorage {
                        override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int =
                            be.receiveEnergy(maxReceive, simulate)
                        override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int = 0
                        override fun getEnergyStored(): Int = be.energy
                        override fun getMaxEnergyStored(): Int = be.capacity
                        override fun canExtract(): Boolean = false
                        override fun canReceive(): Boolean = true
                    }
                },
            )

            event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.BLOCK,
                ADVANCED_CAPSULE_BE.get(),
                { be, side ->
                    val back = be.blockState.getValue(AdvancedProcessingChamberBlock.FACING).opposite
                    if (side != back) null
                    else object : IEnergyStorage {
                        override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int =
                            be.receiveEnergy(maxReceive, simulate)
                        override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int = 0
                        override fun getEnergyStored(): Int = be.energy
                        override fun getMaxEnergyStored(): Int = be.capacity
                        override fun canExtract(): Boolean = false
                        override fun canReceive(): Boolean = true
                    }
                },
            )

            event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
                CAPSULE_BE.get(),
                { be, side ->
                    net.neoforged.neoforge.items.wrapper.SidedInvWrapper(be, side)
                },
            )

            event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
                ADVANCED_CAPSULE_BE.get(),
                { be, side ->
                    net.neoforged.neoforge.items.wrapper.SidedInvWrapper(be, side)
                },
            )
        }
    }
}
