package com.nbp.processing_chamber.neoforge

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.block.AdvancedProcessingChamberBlock
import com.nbp.processing_chamber.block.ProcessingChamberBlock
import com.nbp.processing_chamber.block.entity.ModBlockEntities
import com.nbp.processing_chamber.block.entity.client.CapsuleBlockEntityRenderer
import com.nbp.processing_chamber.client.screen.ProcessingChamberScreen
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.event.RenderHighlightEvent
import net.neoforged.neoforge.common.NeoForge
import kotlin.math.sqrt

@EventBusSubscriber(modid = ProcessingChamber.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ProcessingChamberNeoForgeClient {
    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork {
            ItemBlockRenderTypes.setRenderLayer(
                ProcessingChamberNeoForgeRegistries.PROCESSING_CHAMBER.get(),
                RenderType.translucent(),
            )
            ItemBlockRenderTypes.setRenderLayer(
                ProcessingChamberNeoForgeRegistries.ADVANCED_PROCESSING_CHAMBER.get(),
                RenderType.translucent(),
            )
            NeoForge.EVENT_BUS.addListener(::onRenderBlockHighlight)
        }
    }

    @SubscribeEvent
    fun onRegisterRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(ModBlockEntities.CAPSULE_BLOCK_ENTITY, ::CapsuleBlockEntityRenderer)
        event.registerBlockEntityRenderer(ModBlockEntities.ADVANCED_CAPSULE_BLOCK_ENTITY, ::CapsuleBlockEntityRenderer)
    }

    @SubscribeEvent
    fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
        event.register(ProcessingChamberNeoForgeRegistries.PROCESSING_CHAMBER_MENU.get(), ::ProcessingChamberScreen)
    }

    private fun onRenderBlockHighlight(event: RenderHighlightEvent.Block) {
        val level = Minecraft.getInstance().level ?: return
        val pos = event.target.blockPos
        val state = level.getBlockState(pos)
        if (state.block !is ProcessingChamberBlock && state.block !is AdvancedProcessingChamberBlock) return

        val cameraPos = event.camera.position
        val shape = state.getShape(level, pos)
        val vertexConsumer = event.multiBufferSource.getBuffer(RenderType.lines())
        renderShapeOutline(
            event.poseStack,
            vertexConsumer,
            shape,
            pos.x - cameraPos.x,
            pos.y - cameraPos.y,
            pos.z - cameraPos.z,
            0.02F,
            0.025F,
            0.03F,
            0.9F,
        )
        event.setCanceled(true)
    }

    private fun renderShapeOutline(
        poseStack: PoseStack,
        consumer: VertexConsumer,
        shape: VoxelShape,
        x: Double,
        y: Double,
        z: Double,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
    ) {
        val pose = poseStack.last()
        shape.forAllEdges { minX, minY, minZ, maxX, maxY, maxZ ->
            val dx = maxX - minX
            val dy = maxY - minY
            val dz = maxZ - minZ
            val length = sqrt(dx * dx + dy * dy + dz * dz)
            if (length <= 0.0) return@forAllEdges

            val normalX = (dx / length).toFloat()
            val normalY = (dy / length).toFloat()
            val normalZ = (dz / length).toFloat()

            consumer.addVertex(pose, (minX + x).toFloat(), (minY + y).toFloat(), (minZ + z).toFloat())
                .setColor(red, green, blue, alpha)
                .setNormal(pose, normalX, normalY, normalZ)
            consumer.addVertex(pose, (maxX + x).toFloat(), (maxY + y).toFloat(), (maxZ + z).toFloat())
                .setColor(red, green, blue, alpha)
                .setNormal(pose, normalX, normalY, normalZ)
        }
    }
}
