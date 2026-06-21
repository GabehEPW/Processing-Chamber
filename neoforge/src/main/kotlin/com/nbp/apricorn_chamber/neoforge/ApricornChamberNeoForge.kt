package com.nbp.apricorn_chamber.neoforge

import com.nbp.apricorn_chamber.ApricornChamber
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod(ApricornChamber.MOD_ID)
class ApricornChamberNeoForge(modEventBus: IEventBus) {
    init {
        ApricornChamberNeoForgeRegistries.register(modEventBus)
        ApricornChamber.init()
    }
}
