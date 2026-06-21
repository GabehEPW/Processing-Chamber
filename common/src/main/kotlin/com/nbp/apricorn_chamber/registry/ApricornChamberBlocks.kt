package com.nbp.apricorn_chamber.registry

import com.nbp.apricorn_chamber.ApricornChamber
import com.nbp.apricorn_chamber.block.ApricornChamberBlock
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object ApricornChamberBlocks {
    const val APRICORN_CHAMBER_NAME = "apricorn_chamber"

    val APRICORN_CHAMBER_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ApricornChamber.MOD_ID, APRICORN_CHAMBER_NAME)

    fun createApricornChamberBlock(): ApricornChamberBlock =
        ApricornChamberBlock(ApricornChamberBlock.properties())

    fun createApricornChamberItem(block: Block): BlockItem =
        BlockItem(block, Item.Properties())
}
