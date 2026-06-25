package com.nbp.processing_chamber.registry

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.item.TooltipItem
import com.nbp.processing_chamber.item.ProcessorUpgradeKitItem
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

object ProcessingChamberItems {
    const val PROCESSOR_UPGRADE_KIT_NAME = "processor_upgrade_kit"
    const val OVERCLOCK_CARD_NAME = "overclock_card"
    const val FORTUNE_CARD_NAME = "fortune_card"
    const val OPTIMIZATION_CARD_NAME = "optimization_card"

    val PROCESSOR_UPGRADE_KIT_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, PROCESSOR_UPGRADE_KIT_NAME)
    val OVERCLOCK_CARD_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, OVERCLOCK_CARD_NAME)
    val FORTUNE_CARD_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, FORTUNE_CARD_NAME)
    val OPTIMIZATION_CARD_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, OPTIMIZATION_CARD_NAME)

    fun createProcessorUpgradeKitItem(): Item =
        ProcessorUpgradeKitItem(Item.Properties())

    fun createTooltipItem(name: String): Item =
        TooltipItem(
            Item.Properties(),
            cardTooltipKeys(name),
        )

    private fun cardTooltipKeys(name: String): List<String> {
        val prefix = "tooltip.${ProcessingChamber.MOD_ID}.$name"
        return when (name) {
            FORTUNE_CARD_NAME -> listOf(
                "$prefix.description",
                "$prefix.effect_1",
                "$prefix.effect_2",
                "$prefix.effect_3",
                "tooltip.${ProcessingChamber.MOD_ID}.card_max",
            )
            else -> listOf(
                "$prefix.description",
                "$prefix.effect_1",
                "$prefix.effect_2",
                "tooltip.${ProcessingChamber.MOD_ID}.card_max",
            )
        }
    }
}
