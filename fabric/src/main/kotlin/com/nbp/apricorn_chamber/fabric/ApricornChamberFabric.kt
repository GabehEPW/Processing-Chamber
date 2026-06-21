package com.nbp.cobblemon_bands.fabric

import com.nbp.cobblemon_bands.CobblemonBands
import net.fabricmc.api.ModInitializer

class CobblemonBandsFabric : ModInitializer {
    override fun onInitialize() {
        CobblemonBands.init()
    }
}
