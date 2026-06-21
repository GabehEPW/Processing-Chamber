package com.nbp.apricorn_chamber.fabric

import com.nbp.apricorn_chamber.ApricornChamber
import net.fabricmc.api.ModInitializer

class ApricornChamberFabric : ModInitializer {
    override fun onInitialize() {
        ApricornChamberFabricRegistries.init()
        ApricornChamber.init()
    }
}
