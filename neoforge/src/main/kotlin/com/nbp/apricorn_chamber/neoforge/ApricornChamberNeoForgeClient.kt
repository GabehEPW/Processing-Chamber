package com.nbp.apricorn_chamber.neoforge

import com.nbp.apricorn_chamber.ApricornChamber
import com.nbp.apricorn_chamber.block.entity.ModBlockEntities
import com.nbp.apricorn_chamber.block.entity.client.CapsuleBlockEntityRenderer
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@EventBusSubscriber(modid = ApricornChamber.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ApricornChamberNeoForgeClient {
    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork {
            ItemBlockRenderTypes.setRenderLayer(
                ApricornChamberNeoForgeRegistries.APRICORN_CHAMBER.get(),
                RenderType.translucent(),
            )
        }
    }

    @SubscribeEvent
    fun onRegisterRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(ModBlockEntities.CAPSULE_BLOCK_ENTITY, ::CapsuleBlockEntityRenderer)
    }
}
