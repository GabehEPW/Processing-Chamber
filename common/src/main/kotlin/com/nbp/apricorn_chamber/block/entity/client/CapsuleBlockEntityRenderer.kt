package com.nbp.apricorn_chamber.block.entity.client

import com.mojang.blaze3d.vertex.PoseStack
import com.nbp.apricorn_chamber.block.entity.CapsuleBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.item.ItemDisplayContext

class CapsuleBlockEntityRenderer(context: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<CapsuleBlockEntity> {

    override fun render(
        be: CapsuleBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val stack = be.getItem(0)
        if (stack.isEmpty) return

        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)

        val scale = 0.6f
        poseStack.scale(scale, scale, scale)

        val time = be.level?.gameTime?.toFloat() ?: 0f
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(time * 2f + partialTick * 2f))

        Minecraft.getInstance().itemRenderer.renderStatic(
            stack,
            ItemDisplayContext.GROUND,
            packedLight,
            packedOverlay,
            poseStack,
            bufferSource,
            be.level,
            0,
        )

        poseStack.popPose()
    }
}
