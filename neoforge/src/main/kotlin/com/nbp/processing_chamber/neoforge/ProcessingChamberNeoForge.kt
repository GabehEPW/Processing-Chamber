package com.nbp.processing_chamber.neoforge

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.config.ProcessingChamberConfig
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.loading.FMLPaths

@Mod(ProcessingChamber.MOD_ID)
class ProcessingChamberNeoForge(modEventBus: IEventBus) {
    init {
        ProcessingChamberConfig.configDir = FMLPaths.CONFIGDIR.get()
        ProcessingChamberConfig.load()
        ProcessingChamberNeoForgeRegistries.register(modEventBus)
        ProcessingChamber.init()
    }
}
