package com.nbp.processing_chamber.neoforge

import com.nbp.processing_chamber.ProcessingChamber
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod(ProcessingChamber.MOD_ID)
class ProcessingChamberNeoForge(modEventBus: IEventBus) {
    init {
        ProcessingChamberNeoForgeRegistries.register(modEventBus)
        ProcessingChamber.init()
    }
}
