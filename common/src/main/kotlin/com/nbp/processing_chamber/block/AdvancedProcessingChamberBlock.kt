package com.nbp.processing_chamber.block

import com.mojang.serialization.MapCodec
import com.nbp.processing_chamber.block.entity.CapsuleBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.Containers
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class AdvancedProcessingChamberBlock(properties: Properties) : BaseEntityBlock(properties) {
    init {
        registerDefaultState(defaultBlockState().setValue(FACING, net.minecraft.core.Direction.NORTH))
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
    ): VoxelShape = SHAPES[state.getValue(FACING)]!!

    override fun getCollisionShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPES[state.getValue(FACING)]!!

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

    override fun playerDestroy(
        level: Level,
        player: Player,
        pos: BlockPos,
        state: BlockState,
        blockEntity: BlockEntity?,
        tool: ItemStack,
    ) {
        if (!level.isClientSide && !player.abilities.instabuild) {
            val stack = ItemStack(this)
            (blockEntity as? CapsuleBlockEntity)?.saveEnergyToStack(stack)
            popResource(level, pos, stack)
        }
    }

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        super.setPlacedBy(level, pos, state, placer, stack)
        (level.getBlockEntity(pos) as? CapsuleBlockEntity)?.loadEnergyFromStack(stack)
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: net.minecraft.world.InteractionHand,
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
        if (level.isClientSide) return InteractionResult.SUCCESS
        player.openMenu(be)
        return InteractionResult.SUCCESS
    }

    override fun createBlockStateDefinition(builder: net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
        val CODEC: MapCodec<AdvancedProcessingChamberBlock> = simpleCodec(::AdvancedProcessingChamberBlock)
        var BLOCK_ENTITY_TYPE: BlockEntityType<out BlockEntity>? = null

        private val BOXES = listOf(
            listOf(0.25, 0.0, 0.25, 0.75, 0.125, 0.8125),
            listOf(0.0625, 0.0, 0.25, 0.25, 0.0625, 0.8125),
            listOf(0.75, 0.0, 0.25, 0.9375, 0.0625, 0.8125),
            listOf(0.1875, 0.0, 0.125, 0.8125, 0.0625, 0.25),
            listOf(0.1875, 0.0, 0.8125, 0.8125, 0.0625, 0.9375),
            listOf(0.1875, 0.0625, 0.8125, 0.8125, 0.125, 1.0),
            listOf(0.1875, 0.0625, 0.0625, 0.8125, 0.125, 0.25),
            listOf(0.75, 0.0625, 0.25, 1.0, 0.125, 0.8125),
            listOf(0.0, 0.0625, 0.25, 0.25, 0.125, 0.8125),
            listOf(0.8125, 0.0625, 0.8125, 0.9375, 0.1875, 0.9375),
            listOf(0.8125, 0.0625, 0.125, 0.9375, 0.1875, 0.25),
            listOf(0.0625, 0.0625, 0.125, 0.1875, 0.1875, 0.25),
            listOf(0.125, 0.1875, 0.1875, 0.1875, 0.75, 0.25),
            listOf(0.125, 0.1875, 0.8125, 0.1875, 0.75, 0.875),
            listOf(0.8125, 0.1875, 0.8125, 0.875, 0.75, 0.875),
            listOf(0.8125, 0.1875, 0.1875, 0.875, 0.75, 0.25),
            listOf(0.0, 0.125, 0.25, 0.0625, 0.25, 0.8125),
            listOf(0.9375, 0.125, 0.25, 1.0, 0.25, 0.8125),
            listOf(0.875, 0.125, 0.75, 0.9375, 0.25, 0.8125),
            listOf(0.875, 0.125, 0.25, 0.9375, 0.25, 0.3125),
            listOf(0.0625, 0.125, 0.25, 0.125, 0.25, 0.3125),
            listOf(0.0625, 0.125, 0.75, 0.125, 0.25, 0.8125),
            listOf(0.1875, 0.125, 0.9375, 0.8125, 0.8125, 1.0),
            listOf(0.1875, 0.125, 0.0625, 0.8125, 0.25, 0.125),
            listOf(0.1875, 0.125, 0.125, 0.25, 0.25, 0.1875),
            listOf(0.75, 0.125, 0.125, 0.8125, 0.25, 0.1875),
            listOf(0.1875, 0.125, 0.875, 0.8125, 0.8125, 0.9375),
            listOf(0.125, 0.125, 0.25, 0.1875, 0.8125, 0.8125),
            listOf(0.1875, 0.125, 0.1875, 0.8125, 0.8125, 0.25),
            listOf(0.0625, 0.75, 0.125, 0.1875, 0.875, 0.25),
            listOf(0.8125, 0.75, 0.125, 0.9375, 0.875, 0.25),
            listOf(0.8125, 0.75, 0.8125, 0.9375, 0.875, 0.9375),
            listOf(0.0625, 0.75, 0.8125, 0.1875, 0.875, 0.9375),
            listOf(0.1875, 0.75, 0.0625, 0.8125, 0.875, 0.125),
            listOf(0.1875, 0.8125, 0.8125, 0.8125, 0.875, 1.0),
            listOf(0.0, 0.75, 0.25, 0.0625, 0.875, 0.8125),
            listOf(0.9375, 0.75, 0.25, 1.0, 0.875, 0.8125),
            listOf(0.125, 0.875, 0.1875, 0.875, 0.9375, 0.875),
            listOf(0.875, 0.875, 0.25, 0.9375, 0.9375, 0.8125),
            listOf(0.0625, 0.875, 0.25, 0.125, 0.9375, 0.8125),
            listOf(0.1875, 0.875, 0.125, 0.8125, 0.9375, 0.1875),
            listOf(0.1875, 0.875, 0.875, 0.8125, 0.9375, 0.9375),
            listOf(0.1875, 0.9375, 0.1875, 0.8125, 1.0, 0.875),
            listOf(0.8125, 0.9375, 0.25, 0.875, 1.0, 0.8125),
            listOf(0.125, 0.9375, 0.25, 0.1875, 1.0, 0.8125),
            listOf(0.4375, 0.625, 0.0, 0.5625, 0.875, 0.0625),
            listOf(0.375, 0.6875, 0.0, 0.4375, 0.8125, 0.0625),
            listOf(0.5625, 0.6875, 0.0, 0.625, 0.8125, 0.0625),
            listOf(0.4375, 0.0, 0.0625, 0.5625, 0.0625, 0.125),
            listOf(0.0625, 0.0625, 0.8125, 0.1875, 0.1875, 0.9375),
            listOf(0.8125, 0.125, 0.25, 0.875, 0.8125, 0.8125),
            listOf(0.25, 0.125, 0.3125, 0.75, 0.1875, 0.75),
            listOf(0.25, 0.1875, 0.3125, 0.75, 0.25, 0.375),
            listOf(0.6875, 0.1875, 0.375, 0.75, 0.25, 0.6875),
            listOf(0.25, 0.1875, 0.375, 0.3125, 0.25, 0.6875),
            listOf(0.375, 0.125, 0.0, 0.625, 0.1875, 0.0625),
            listOf(0.5625, 0.1875, 0.0, 0.625, 0.25, 0.0625),
            listOf(0.375, 0.1875, 0.0, 0.4375, 0.25, 0.0625),
            listOf(0.4375, 0.0625, 0.0, 0.5625, 0.125, 0.0625),
            listOf(0.25, 0.1875, 0.6875, 0.75, 0.25, 0.75),
        )

        private val SHAPES: Map<Direction, VoxelShape> = run {
            fun build(rotations: Int): VoxelShape {
                return BOXES.fold(Shapes.empty()) { shape, b ->
                    var x1 = b[0]; var y1 = b[1]; var z1 = b[2]; var x2 = b[3]; var y2 = b[4]; var z2 = b[5]
                    repeat(rotations) {
                        val nx1 = 1.0 - z2; val nz1 = x1
                        val nx2 = 1.0 - z1; val nz2 = x2
                        x1 = nx1; z1 = nz1; x2 = nx2; z2 = nz2
                    }
                    Shapes.join(shape, Shapes.box(x1, y1, z1, x2, y2, z2), BooleanOp.OR)
                }
            }
            mapOf(
                Direction.NORTH to build(0),
                Direction.SOUTH to build(2),
                Direction.EAST to build(1),
                Direction.WEST to build(3),
            )
        }

        fun properties(): Properties = Properties.of()
            .strength(3.0f, 6.0f)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops()
            .noOcclusion()
    }
}
