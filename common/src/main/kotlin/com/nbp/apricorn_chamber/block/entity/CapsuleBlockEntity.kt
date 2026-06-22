package com.nbp.apricorn_chamber.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class CapsuleBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type, pos, state), WorldlyContainer {

    companion object {
        const val CAPACITY = 20000
        const val ENERGY_PER_TICK = 10
        const val PROCESS_TIME = 100
    }

    var energy: Int = 0
        private set
    var processProgress: Int = 0
    private val items = NonNullList.withSize(2, ItemStack.EMPTY)

    fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        val accepted = minOf(maxReceive, CAPACITY - energy)
        if (!simulate) {
            energy += accepted
            setChanged()
        }
        return accepted
    }

    fun extractEnergy(maxExtract: Int, simulate: Boolean): Int = 0

    fun tick() {
        if (level == null || level!!.isClientSide) return

        if (canProcess()) {
            if (energy >= ENERGY_PER_TICK) {
                energy -= ENERGY_PER_TICK
                processProgress++
                setChanged()

                if (processProgress >= PROCESS_TIME) {
                    process()
                    processProgress = 0
                }
            }
        } else if (processProgress > 0) {
            processProgress = 0
            setChanged()
        }
    }

    private fun canProcess(): Boolean {
        val input = items[0]
        if (input.isEmpty) return false
        val result = getResult(input)
        if (result.isEmpty) return false

        val output = items[1]
        if (output.isEmpty) return true
        return ItemStack.isSameItemSameComponents(output, result) &&
            output.count + result.count <= output.maxStackSize
    }

    private fun process() {
        val input = items[0]
        val result = getResult(input)
        if (result.isEmpty) return

        input.shrink(1)
        val output = items[1]
        if (output.isEmpty) {
            items[1] = result.copy()
        } else {
            output.grow(result.count)
        }
    }

    private fun getResult(input: ItemStack): ItemStack {
        val inputId = BuiltInRegistries.ITEM.getKey(input.item)?.toString() ?: ""
        val seedApricornMap = mapOf(
            "cobblemon:black_apricorn_seed" to "cobblemon:black_apricorn",
            "cobblemon:blue_apricorn_seed" to "cobblemon:blue_apricorn",
            "cobblemon:green_apricorn_seed" to "cobblemon:green_apricorn",
            "cobblemon:pink_apricorn_seed" to "cobblemon:pink_apricorn",
            "cobblemon:red_apricorn_seed" to "cobblemon:red_apricorn",
            "cobblemon:white_apricorn_seed" to "cobblemon:white_apricorn",
            "cobblemon:yellow_apricorn_seed" to "cobblemon:yellow_apricorn",
        )
        val resultId = seedApricornMap[inputId] ?: return ItemStack.EMPTY

        val item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(resultId))
        if (item == BuiltInRegistries.ITEM.get(ResourceLocation.tryParse("minecraft:air"))) return ItemStack.EMPTY
        return ItemStack(item, 1)
    }

    // --- WorldlyContainer ---

    override fun getContainerSize(): Int = 2
    override fun isEmpty(): Boolean = items.all { it.isEmpty }
    override fun getItem(slot: Int): ItemStack = items[slot]
    override fun removeItem(slot: Int, amount: Int): ItemStack = ContainerHelper.removeItem(items, slot, amount)
    override fun removeItemNoUpdate(slot: Int): ItemStack = ContainerHelper.takeItem(items, slot)
    override fun setItem(slot: Int, stack: ItemStack) {
        items[slot] = stack
        if (stack.count > maxStackSize) stack.count = maxStackSize
        setChanged()
    }
    override fun stillValid(player: net.minecraft.world.entity.player.Player): Boolean =
        if (level?.getBlockEntity(worldPosition) != this) false
        else player.distanceToSqr(
            worldPosition.x + 0.5, worldPosition.y + 0.5, worldPosition.z + 0.5
        ) <= 64.0
    override fun clearContent() = items.fill(ItemStack.EMPTY)
    override fun getSlotsForFace(side: Direction?): IntArray = intArrayOf(0, 1)
    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, side: Direction?): Boolean =
        slot == 0 && canProcess()
    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, side: Direction?): Boolean = slot == 1

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.putInt("energy", energy)
        tag.putInt("progress", processProgress)
        ContainerHelper.saveAllItems(tag, items, registries)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        energy = tag.getInt("energy")
        processProgress = tag.getInt("progress")
        ContainerHelper.loadAllItems(tag, items, registries)
    }

    override fun getUpdatePacket(): net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithFullMetadata(registries)
    }
}
