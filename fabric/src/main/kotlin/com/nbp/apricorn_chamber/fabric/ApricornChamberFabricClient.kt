package com.nbp.apricorn_chamber.fabric

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.renderer.RenderType

class ApricornChamberFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(
            ApricornChamberFabricRegistries.APRICORN_CHAMBER,
            RenderType.translucent(),
        )
    }
}
