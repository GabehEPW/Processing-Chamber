package com.nbp.apricorn_chamber.fabric

import com.nbp.apricorn_chamber.registry.ApricornChamberBlocks
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Registry
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block

object ApricornChamberFabricRegistries {
    val APRICORN_CHAMBER: Block = Registry.register(
        BuiltInRegistries.BLOCK,
        ApricornChamberBlocks.APRICORN_CHAMBER_ID,
        ApricornChamberBlocks.createApricornChamberBlock(),
    )

    val APRICORN_CHAMBER_ITEM: BlockItem = Registry.register(
        BuiltInRegistries.ITEM,
        ApricornChamberBlocks.APRICORN_CHAMBER_ID,
        ApricornChamberBlocks.createApricornChamberItem(APRICORN_CHAMBER),
    )

    fun init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register { entries ->
            entries.accept(APRICORN_CHAMBER_ITEM)
        }
    }
}
