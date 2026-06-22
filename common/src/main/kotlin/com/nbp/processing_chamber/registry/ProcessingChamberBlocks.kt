package com.nbp.processing_chamber.registry

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.block.ProcessingChamberBlock
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object ProcessingChamberBlocks {
    const val PROCESSING_CHAMBER_NAME = "processing_chamber"

    val PROCESSING_CHAMBER_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, PROCESSING_CHAMBER_NAME)

    fun createProcessingChamberBlock(): ProcessingChamberBlock =
        ProcessingChamberBlock(ProcessingChamberBlock.properties())

    fun createProcessingChamberItem(block: Block): BlockItem =
        BlockItem(block, Item.Properties())
}
