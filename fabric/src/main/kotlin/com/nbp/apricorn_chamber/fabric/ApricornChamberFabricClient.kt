package com.nbp.apricorn_chamber.fabric

import com.nbp.apricorn_chamber.block.entity.ModBlockEntities
import com.nbp.apricorn_chamber.block.entity.client.CapsuleBlockEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers

class ApricornChamberFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(
            ApricornChamberFabricRegistries.APRICORN_CHAMBER,
            RenderType.translucent(),
        )

        BlockEntityRenderers.register(
            ModBlockEntities.CAPSULE_BLOCK_ENTITY,
            ::CapsuleBlockEntityRenderer,
        )
    }
}
