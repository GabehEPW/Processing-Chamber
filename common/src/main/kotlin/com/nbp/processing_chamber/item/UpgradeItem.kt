package com.nbp.processing_chamber.item

import com.nbp.processing_chamber.block.ProcessingChamberBlock
import com.nbp.processing_chamber.block.entity.CapsuleBlockEntity
import com.nbp.processing_chamber.registry.ProcessingChamberBlocks
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class UpgradeItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player ?: return InteractionResult.PASS
        val level = context.level
        val pos = context.clickedPos
        val state = level.getBlockState(pos)
        val stack = context.itemInHand

        if (state.block !is ProcessingChamberBlock) return InteractionResult.PASS

        if (level.isClientSide) return InteractionResult.SUCCESS

        val oldBe = level.getBlockEntity(pos) as? CapsuleBlockEntity ?: return InteractionResult.FAIL
        val oldData = oldBe.saveWithoutMetadata(level.registryAccess())
        oldBe.clearContent()

        val advancedBlock = BuiltInRegistries.BLOCK.get(ProcessingChamberBlocks.ADVANCED_PROCESSING_CHAMBER_ID)
        val newState = advancedBlock.defaultBlockState()
            .setValue(ProcessingChamberBlock.FACING, state.getValue(ProcessingChamberBlock.FACING))
        level.setBlock(pos, newState, 3)

        val newBe = level.getBlockEntity(pos) as? CapsuleBlockEntity ?: return InteractionResult.FAIL
        newBe.loadData(oldData, level.registryAccess())
        newBe.setChanged()

        if (!player.abilities.instabuild) stack.shrink(1)
        return InteractionResult.SUCCESS
    }
}