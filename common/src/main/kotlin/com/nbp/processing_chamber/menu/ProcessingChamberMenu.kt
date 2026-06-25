package com.nbp.processing_chamber.menu

import com.nbp.processing_chamber.block.entity.CapsuleBlockEntity
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class ProcessingChamberMenu : AbstractContainerMenu {
    private val container: Container
    private val data: ContainerData

    constructor(containerId: Int, playerInventory: Inventory) : this(
        containerId,
        playerInventory,
        SimpleContainer(CapsuleBlockEntity.CONTAINER_SIZE),
        SimpleContainerData(CapsuleBlockEntity.DATA_COUNT),
    )

    constructor(
        containerId: Int,
        playerInventory: Inventory,
        container: Container,
        data: ContainerData,
    ) : super(ModMenus.PROCESSING_CHAMBER_MENU, containerId) {
        this.container = container
        this.data = data

        checkContainerSize(container, CapsuleBlockEntity.CONTAINER_SIZE)
        container.startOpen(playerInventory.player)

        addSlot(InputSlot(container, CapsuleBlockEntity.SLOT_INPUT, 52, 62))

        for (row in 0..2) {
            for (column in 0..2) {
                val slot = CapsuleBlockEntity.SLOT_OUTPUT_START + column + row * 3
                addSlot(OutputSlot(container, slot, 94 + column * 20, 41 + row * 20))
            }
        }

        for (row in 0..2) {
            val slot = CapsuleBlockEntity.SLOT_UPGRADE_START + row
            addSlot(UpgradeSlot(container, slot, 160, 41 + row * 20))
        }

        for (row in 0..2) {
            for (column in 0..8) {
                addSlot(Slot(playerInventory, column + row * 9 + 9, 16 + column * 18, 118 + row * 18))
            }
        }

        for (column in 0..8) {
            addSlot(Slot(playerInventory, column, 16 + column * 18, 176))
        }

        addDataSlots(data)
    }

    val energy: Int
        get() = data.get(CapsuleBlockEntity.DATA_ENERGY)

    val capacity: Int
        get() = data.get(CapsuleBlockEntity.DATA_CAPACITY)

    val progress: Int
        get() = data.get(CapsuleBlockEntity.DATA_PROGRESS)

    val processTime: Int
        get() = data.get(CapsuleBlockEntity.DATA_PROCESS_TIME)

    val energyPerTick: Int
        get() = data.get(CapsuleBlockEntity.DATA_ENERGY_PER_TICK)

    val fortuneChancePercent: Int
        get() = data.get(CapsuleBlockEntity.DATA_FORTUNE_CHANCE_PERCENT)

    override fun stillValid(player: Player): Boolean = container.stillValid(player)

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        val slot = slots[index]
        if (!slot.hasItem()) return ItemStack.EMPTY

        val stack = slot.item
        val copy = stack.copy()
        val containerSlots = CapsuleBlockEntity.CONTAINER_SIZE

        if (index < containerSlots) {
            if (!moveItemStackTo(stack, containerSlots, slots.size, true)) return ItemStack.EMPTY
        } else if (CapsuleBlockEntity.isValidUpgrade(stack)) {
            if (!moveSingleUpgradeStack(stack)) {
                return ItemStack.EMPTY
            }
        } else if (CapsuleBlockEntity.isValidSeed(stack)) {
            if (!moveItemStackTo(
                    stack,
                    CapsuleBlockEntity.SLOT_INPUT,
                    CapsuleBlockEntity.SLOT_INPUT + 1,
                    false,
                )
            ) {
                return ItemStack.EMPTY
            }
        } else {
            return ItemStack.EMPTY
        }

        if (stack.isEmpty) slot.setByPlayer(ItemStack.EMPTY) else slot.setChanged()
        return copy
    }

    private fun moveSingleUpgradeStack(stack: ItemStack): Boolean {
        for (index in CapsuleBlockEntity.SLOT_UPGRADE_START..CapsuleBlockEntity.SLOT_UPGRADE_END) {
            val slot = slots[index]
            if (slot.hasItem() || !slot.mayPlace(stack)) continue

            val single = stack.copy()
            single.count = 1
            slot.setByPlayer(single)
            slot.setChanged()
            stack.shrink(1)
            return true
        }
        return false
    }

    override fun removed(player: Player) {
        super.removed(player)
        container.stopOpen(player)
    }

    private class InputSlot(container: Container, slot: Int, x: Int, y: Int) : Slot(container, slot, x, y) {
        override fun mayPlace(stack: ItemStack): Boolean = CapsuleBlockEntity.isValidSeed(stack)
    }

    private class OutputSlot(container: Container, slot: Int, x: Int, y: Int) : Slot(container, slot, x, y) {
        override fun mayPlace(stack: ItemStack): Boolean = false
    }

    private class UpgradeSlot(
        container: Container,
        private val machineSlot: Int,
        x: Int,
        y: Int,
    ) : Slot(container, machineSlot, x, y) {
        override fun mayPlace(stack: ItemStack): Boolean {
            val machine = container as? CapsuleBlockEntity ?: return CapsuleBlockEntity.isValidUpgrade(stack)
            return machine.canInsertUpgrade(stack, machineSlot)
        }

        override fun getMaxStackSize(): Int = 1

        override fun getMaxStackSize(stack: ItemStack): Int = 1
    }
}
