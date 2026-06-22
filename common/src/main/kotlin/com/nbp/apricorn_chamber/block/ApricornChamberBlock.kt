package com.nbp.apricorn_chamber.block

import com.mojang.serialization.MapCodec
import com.nbp.apricorn_chamber.block.entity.CapsuleBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.Containers
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class ApricornChamberBlock(properties: Properties) : BaseEntityBlock(properties) {
    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE

    override fun getCollisionShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = true

    override fun getLightBlock(state: BlockState, level: BlockGetter, pos: BlockPos): Int = 0

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return BLOCK_ENTITY_TYPE?.create(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        level: Level,
        state: BlockState,
        type: BlockEntityType<T>,
    ): BlockEntityTicker<T>? {
        val beType = BLOCK_ENTITY_TYPE ?: return null
        if (!level.isClientSide) {
            return createTickerHelper(type, beType as BlockEntityType<T>) { _, _, _, be ->
                (be as CapsuleBlockEntity).tick()
            }
        }
        return null
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        if (!state.`is`(newState.block)) {
            val be = level.getBlockEntity(pos) as? CapsuleBlockEntity
            if (be != null) {
                Containers.dropContents(level, pos, be)
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    companion object {
        val CODEC: MapCodec<ApricornChamberBlock> = simpleCodec(::ApricornChamberBlock)
        var BLOCK_ENTITY_TYPE: BlockEntityType<out BlockEntity>? = null

        private val SHAPE = box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)

        fun properties(): Properties = Properties.of()
            .strength(3.5f)
            .sound(SoundType.GLASS)
            .noOcclusion()
    }
}
