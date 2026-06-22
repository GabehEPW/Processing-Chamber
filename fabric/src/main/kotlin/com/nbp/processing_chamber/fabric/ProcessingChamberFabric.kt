package com.nbp.processing_chamber.fabric

import com.nbp.processing_chamber.ProcessingChamber
import net.fabricmc.api.ModInitializer

class ProcessingChamberFabric : ModInitializer {
    override fun onInitialize() {
        ProcessingChamberFabricRegistries.init()
        ProcessingChamber.init()
    }
}
