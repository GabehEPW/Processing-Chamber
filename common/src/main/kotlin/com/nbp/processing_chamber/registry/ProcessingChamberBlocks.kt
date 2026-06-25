package com.nbp.processing_chamber.registry

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.block.AdvancedProcessingChamberBlock
import com.nbp.processing_chamber.block.ProcessingChamberBlock
import com.nbp.processing_chamber.item.MachineBlockItem
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object ProcessingChamberBlocks {
    const val PROCESSING_CHAMBER_NAME = "resource_processor"
    const val ADVANCED_PROCESSING_CHAMBER_NAME = "advanced_resource_processor"

    val PROCESSING_CHAMBER_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, PROCESSING_CHAMBER_NAME)
    val ADVANCED_PROCESSING_CHAMBER_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, ADVANCED_PROCESSING_CHAMBER_NAME)

    fun createProcessingChamberBlock(): ProcessingChamberBlock =
        ProcessingChamberBlock(ProcessingChamberBlock.properties())

    fun createAdvancedProcessingChamberBlock(): AdvancedProcessingChamberBlock =
        AdvancedProcessingChamberBlock(AdvancedProcessingChamberBlock.properties())

    fun createProcessingChamberItem(block: Block): BlockItem =
        MachineBlockItem(
            block,
            Item.Properties(),
            listOf(
                "tooltip.${ProcessingChamber.MOD_ID}.resource_processor.description",
                "tooltip.${ProcessingChamber.MOD_ID}.resource_processor.energy",
                "tooltip.${ProcessingChamber.MOD_ID}.resource_processor.consumption",
                "tooltip.${ProcessingChamber.MOD_ID}.resource_processor.time",
                "tooltip.${ProcessingChamber.MOD_ID}.resource_processor.upgrades",
            ),
        )

    fun createAdvancedProcessingChamberItem(block: Block): BlockItem =
        MachineBlockItem(
            block,
            Item.Properties(),
            listOf(
                "tooltip.${ProcessingChamber.MOD_ID}.advanced_resource_processor.description",
                "tooltip.${ProcessingChamber.MOD_ID}.advanced_resource_processor.energy",
                "tooltip.${ProcessingChamber.MOD_ID}.advanced_resource_processor.consumption",
                "tooltip.${ProcessingChamber.MOD_ID}.advanced_resource_processor.time",
                "tooltip.${ProcessingChamber.MOD_ID}.advanced_resource_processor.upgrades",
            ),
        )
}
