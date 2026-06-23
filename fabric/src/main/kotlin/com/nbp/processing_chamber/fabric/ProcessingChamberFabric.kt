package com.nbp.processing_chamber.fabric

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.config.ProcessingChamberConfig
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader

class ProcessingChamberFabric : ModInitializer {
    override fun onInitialize() {
        ProcessingChamberConfig.configDir = FabricLoader.getInstance().configDir
        ProcessingChamberConfig.load()
        ProcessingChamberFabricRegistries.init()
        ProcessingChamber.init()
    }
}
