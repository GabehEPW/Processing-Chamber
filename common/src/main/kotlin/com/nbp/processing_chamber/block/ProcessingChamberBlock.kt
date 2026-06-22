package com.nbp.processing_chamber.block

import com.mojang.serialization.MapCodec
import com.nbp.processing_chamber.block.entity.CapsuleBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.Containers
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class ProcessingChamberBlock(properties: Properties) : BaseEntityBlock(properties) {
    init {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH))
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, context.horizontalDirection.opposite)
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)))
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)))
    }

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

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult,
    ): ItemInteractionResult {
        val be = level.getBlockEntity(pos) as? CapsuleBlockEntity ?: return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        if (!be.getItem(0).isEmpty) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        if (!CapsuleBlockEntity.isValidSeed(stack)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        if (level.isClientSide) return ItemInteractionResult.SUCCESS
        be.setItem(0, stack.copyWithCount(1))
        if (!player.abilities.instabuild) stack.shrink(1)
        return ItemInteractionResult.SUCCESS
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hit: BlockHitResult,
    ): InteractionResult {
        val be = level.getBlockEntity(pos) as? CapsuleBlockEntity ?: return InteractionResult.PASS
        val seed = be.getItem(0)
        if (seed.isEmpty) return InteractionResult.PASS
        if (level.isClientSide) return InteractionResult.SUCCESS
        be.setItem(0, ItemStack.EMPTY)
        if (!player.addItem(seed)) {
            player.drop(seed, false)
        }
        return InteractionResult.SUCCESS
    }

    override fun createBlockStateDefinition(builder: net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
        val CODEC: MapCodec<ProcessingChamberBlock> = simpleCodec(::ProcessingChamberBlock)
        var BLOCK_ENTITY_TYPE: BlockEntityType<out BlockEntity>? = null

        private val SHAPE = box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)

        fun properties(): Properties = Properties.of()
            .strength(3.0f, 6.0f)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops()
            .noOcclusion()
    }
}
