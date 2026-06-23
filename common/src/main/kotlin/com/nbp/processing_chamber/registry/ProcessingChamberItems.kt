package com.nbp.processing_chamber.registry

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.item.UpgradeItem
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

object ProcessingChamberItems {
    const val UPGRADE_PROCESSING_CHAMBER_NAME = "upgrade_processing_chamber"

    val UPGRADE_PROCESSING_CHAMBER_ID: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, UPGRADE_PROCESSING_CHAMBER_NAME)

    fun createUpgradeItem(): Item =
        UpgradeItem(Item.Properties())
}
