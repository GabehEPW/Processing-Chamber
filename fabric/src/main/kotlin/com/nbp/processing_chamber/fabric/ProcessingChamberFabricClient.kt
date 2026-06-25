package com.nbp.processing_chamber.fabric

import com.nbp.processing_chamber.block.entity.ModBlockEntities
import com.nbp.processing_chamber.block.entity.client.CapsuleBlockEntityRenderer
import com.nbp.processing_chamber.client.screen.ProcessingChamberScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers

class ProcessingChamberFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(
            ProcessingChamberFabricRegistries.PROCESSING_CHAMBER,
            RenderType.translucent(),
        )
        BlockRenderLayerMap.INSTANCE.putBlock(
            ProcessingChamberFabricRegistries.ADVANCED_PROCESSING_CHAMBER,
            RenderType.translucent(),
        )

        BlockEntityRenderers.register(
            ModBlockEntities.CAPSULE_BLOCK_ENTITY,
            ::CapsuleBlockEntityRenderer,
        )
        BlockEntityRenderers.register(
            ModBlockEntities.ADVANCED_CAPSULE_BLOCK_ENTITY,
            ::CapsuleBlockEntityRenderer,
        )

        MenuScreens.register(
            ProcessingChamberFabricRegistries.PROCESSING_CHAMBER_MENU,
            ::ProcessingChamberScreen,
        )
    }
}
