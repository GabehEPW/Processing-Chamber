package com.nbp.processing_chamber.neoforge

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.block.entity.ModBlockEntities
import com.nbp.processing_chamber.block.entity.client.CapsuleBlockEntityRenderer
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@EventBusSubscriber(modid = ProcessingChamber.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ProcessingChamberNeoForgeClient {
    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork {
            ItemBlockRenderTypes.setRenderLayer(
                ProcessingChamberNeoForgeRegistries.PROCESSING_CHAMBER.get(),
                RenderType.translucent(),
            )
        }
    }

    @SubscribeEvent
    fun onRegisterRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(ModBlockEntities.CAPSULE_BLOCK_ENTITY, ::CapsuleBlockEntityRenderer)
    }
}
