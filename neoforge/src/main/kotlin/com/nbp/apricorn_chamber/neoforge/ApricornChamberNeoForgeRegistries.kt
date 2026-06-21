package com.nbp.apricorn_chamber.neoforge

import com.nbp.apricorn_chamber.ApricornChamber
import com.nbp.apricorn_chamber.registry.ApricornChamberBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ApricornChamberNeoForgeRegistries {
    private val BLOCKS: DeferredRegister<Block> =
        DeferredRegister.create(Registries.BLOCK, ApricornChamber.MOD_ID)

    private val ITEMS: DeferredRegister.Items =
        DeferredRegister.createItems(ApricornChamber.MOD_ID)

    val APRICORN_CHAMBER: DeferredHolder<Block, Block> =
        BLOCKS.register(ApricornChamberBlocks.APRICORN_CHAMBER_NAME, Supplier {
            ApricornChamberBlocks.createApricornChamberBlock()
        })

    val APRICORN_CHAMBER_ITEM: DeferredItem<BlockItem> =
        ITEMS.register(ApricornChamberBlocks.APRICORN_CHAMBER_NAME, Supplier {
            ApricornChamberBlocks.createApricornChamberItem(APRICORN_CHAMBER.get())
        })

    fun register(modEventBus: IEventBus) {
        BLOCKS.register(modEventBus)
        ITEMS.register(modEventBus)
        modEventBus.addListener(::addCreative)
    }

    private fun addCreative(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(APRICORN_CHAMBER_ITEM.get())
        }
    }
}
