package com.nbp.apricorn_chamber.neoforge

import com.nbp.apricorn_chamber.ApricornChamber
import com.nbp.apricorn_chamber.block.ApricornChamberBlock
import com.nbp.apricorn_chamber.block.entity.CapsuleBlockEntity
import com.nbp.apricorn_chamber.block.entity.ModBlockEntities
import com.nbp.apricorn_chamber.registry.ApricornChamberBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ApricornChamberNeoForgeRegistries {
    private val BLOCKS: DeferredRegister<Block> =
        DeferredRegister.create(Registries.BLOCK, ApricornChamber.MOD_ID)

    private val ITEMS: DeferredRegister<Item> =
        DeferredRegister.create(Registries.ITEM, ApricornChamber.MOD_ID)

    private val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ApricornChamber.MOD_ID)

    val APRICORN_CHAMBER: DeferredHolder<Block, Block> =
        BLOCKS.register(ApricornChamberBlocks.APRICORN_CHAMBER_NAME, Supplier {
            ApricornChamberBlocks.createApricornChamberBlock()
        })

    val APRICORN_CHAMBER_ITEM: DeferredHolder<Item, BlockItem> =
        ITEMS.register(ApricornChamberBlocks.APRICORN_CHAMBER_NAME, Supplier {
            BlockItem(APRICORN_CHAMBER.get(), Item.Properties())
        })

    private val CAPSULE_BE: DeferredHolder<BlockEntityType<*>, BlockEntityType<CapsuleBlockEntity>> =
        BLOCK_ENTITIES.register(ApricornChamberBlocks.APRICORN_CHAMBER_NAME, Supplier {
            val type = BlockEntityType.Builder.of(
                { pos, state -> CapsuleBlockEntity(ModBlockEntities.CAPSULE_BLOCK_ENTITY, pos, state) },
                APRICORN_CHAMBER.get(),
            ).build(null)
            ModBlockEntities.CAPSULE_BLOCK_ENTITY = type
            ApricornChamberBlock.BLOCK_ENTITY_TYPE = type
            type
        })

    fun register(modEventBus: IEventBus) {
        BLOCKS.register(modEventBus)
        ITEMS.register(modEventBus)
        BLOCK_ENTITIES.register(modEventBus)
        modEventBus.addListener(::addCreative)

        modEventBus.addListener<RegisterCapabilitiesEvent> { event ->
            event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.BLOCK,
                CAPSULE_BE.get(),
                { be, _ ->
                    object : IEnergyStorage {
                        override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int =
                            be.receiveEnergy(maxReceive, simulate)
                        override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int = 0
                        override fun getEnergyStored(): Int = be.energy
                        override fun getMaxEnergyStored(): Int = CapsuleBlockEntity.CAPACITY
                        override fun canExtract(): Boolean = false
                        override fun canReceive(): Boolean = true
                    }
                },
            )
        }
    }

    private fun addCreative(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(APRICORN_CHAMBER_ITEM.get())
        }
    }
}
