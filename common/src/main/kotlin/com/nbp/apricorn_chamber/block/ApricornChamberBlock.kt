package com.nbp.apricorn_chamber.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class ApricornChamberBlock(properties: Properties) : Block(properties) {
    init {
        registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HALF)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val pos = context.clickedPos
        val level = context.level
        return if (pos.y < level.maxBuildHeight - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER)
        } else {
            null
        }
    }

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        level.setBlock(pos.above(), defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), UPDATE_ALL)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        currentPos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState {
        val half = state.getValue(HALF)
        val expectedDirection = if (half == DoubleBlockHalf.LOWER) Direction.UP else Direction.DOWN

        if (direction == expectedDirection) {
            return if (neighborState.`is`(this) && neighborState.getValue(HALF) != half) {
                state
            } else {
                Blocks.AIR.defaultBlockState()
            }
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos)
    }

    override fun playerWillDestroy(level: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        if (!level.isClientSide) {
            val otherPos = if (state.getValue(HALF) == DoubleBlockHalf.LOWER) pos.above() else pos.below()
            val otherState = level.getBlockState(otherPos)

            if (otherState.`is`(this) && otherState.getValue(HALF) != state.getValue(HALF)) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), UPDATE_SUPPRESS_DROPS or UPDATE_CLIENTS)
                level.levelEvent(player, 2001, otherPos, getId(otherState))
            }
        }

        return super.playerWillDestroy(level, pos, state, player)
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        return if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            val lowerState = level.getBlockState(pos.below())
            lowerState.`is`(this) && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER
        } else {
            super.canSurvive(state, level, pos)
        }
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape {
        return if (state.getValue(HALF) == DoubleBlockHalf.LOWER) LOWER_SHAPE else UPPER_SHAPE
    }

    override fun getCollisionShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = getShape(state, level, pos, context)

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = true

    override fun getLightBlock(state: BlockState, level: BlockGetter, pos: BlockPos): Int = 0

    companion object {
        val HALF = BlockStateProperties.DOUBLE_BLOCK_HALF

        private val LOWER_SHAPE = box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        private val UPPER_SHAPE = box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)

        fun properties(): Properties = Properties.of()
            .strength(3.5f)
            .sound(SoundType.GLASS)
            .noOcclusion()
    }
}
