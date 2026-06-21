package com.nbp.apricorn_chamber.neoforge

import com.nbp.apricorn_chamber.ApricornChamber
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

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
}
