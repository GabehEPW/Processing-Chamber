package com.nbp.processing_chamber.block.entity

import com.nbp.processing_chamber.block.AdvancedProcessingChamberBlock
import com.nbp.processing_chamber.menu.ProcessingChamberMenu
import com.nbp.processing_chamber.config.ProcessingChamberConfig
import com.nbp.processing_chamber.registry.ProcessingChamberItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.roundToInt

class CapsuleBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
    val capacity: Int = 5000,
    val energyPerTick: Int = 15,
    val processTime: Int = 1800,
    val outputAmount: Int = 1,
    val upgradeSlotCount: Int = 1,
    val minProcessTime: Int = 1200,
    val fortuneBonusChance: Float = 0.15f,
) : BlockEntity(type, pos, state), WorldlyContainer, MenuProvider {

    val maxReceive: Int = capacity

    companion object {
        const val SLOT_INPUT = 0
        const val SLOT_OUTPUT_START = 1
        const val SLOT_OUTPUT_END = 9
        const val SLOT_UPGRADE_START = 10
        const val SLOT_UPGRADE_END = 12
        const val MAX_UPGRADE_SLOTS = 3
        const val CONTAINER_SIZE = 13

        const val DATA_ENERGY = 0
        const val DATA_CAPACITY = 1
        const val DATA_PROGRESS = 2
        const val DATA_PROCESS_TIME = 3
        const val DATA_ENERGY_PER_TICK = 4
        const val DATA_FORTUNE_CHANCE_PERCENT = 5
        const val DATA_COUNT = 6
        private const val STACK_ENERGY_KEY = "processing_chamber_energy"

        private val ITEM_MAP = buildMap<String, String> {
            // Apricorn seeds → apricorns
            put("cobblemon:black_apricorn_seed", "cobblemon:black_apricorn")
            put("cobblemon:blue_apricorn_seed", "cobblemon:blue_apricorn")
            put("cobblemon:green_apricorn_seed", "cobblemon:green_apricorn")
            put("cobblemon:pink_apricorn_seed", "cobblemon:pink_apricorn")
            put("cobblemon:red_apricorn_seed", "cobblemon:red_apricorn")
            put("cobblemon:white_apricorn_seed", "cobblemon:white_apricorn")
            put("cobblemon:yellow_apricorn_seed", "cobblemon:yellow_apricorn")

            // Berries → same berry
            put("cobblemon:aguav_berry", "cobblemon:aguav_berry")
            put("cobblemon:apicot_berry", "cobblemon:apicot_berry")
            put("cobblemon:aspear_berry", "cobblemon:aspear_berry")
            put("cobblemon:babiri_berry", "cobblemon:babiri_berry")
            put("cobblemon:belue_berry", "cobblemon:belue_berry")
            put("cobblemon:bluk_berry", "cobblemon:bluk_berry")
            put("cobblemon:charti_berry", "cobblemon:charti_berry")
            put("cobblemon:cheri_berry", "cobblemon:cheri_berry")
            put("cobblemon:chesto_berry", "cobblemon:chesto_berry")
            put("cobblemon:chilan_berry", "cobblemon:chilan_berry")
            put("cobblemon:chople_berry", "cobblemon:chople_berry")
            put("cobblemon:coba_berry", "cobblemon:coba_berry")
            put("cobblemon:colbur_berry", "cobblemon:colbur_berry")
            put("cobblemon:cornn_berry", "cobblemon:cornn_berry")
            put("cobblemon:custap_berry", "cobblemon:custap_berry")
            put("cobblemon:durin_berry", "cobblemon:durin_berry")
            put("cobblemon:eggant_berry", "cobblemon:eggant_berry")
            put("cobblemon:enigma_berry", "cobblemon:enigma_berry")
            put("cobblemon:figy_berry", "cobblemon:figy_berry")
            put("cobblemon:ganlon_berry", "cobblemon:ganlon_berry")
            put("cobblemon:grepa_berry", "cobblemon:grepa_berry")
            put("cobblemon:haban_berry", "cobblemon:haban_berry")
            put("cobblemon:hondew_berry", "cobblemon:hondew_berry")
            put("cobblemon:hopo_berry", "cobblemon:hopo_berry")
            put("cobblemon:iapapa_berry", "cobblemon:iapapa_berry")
            put("cobblemon:jaboca_berry", "cobblemon:jaboca_berry")
            put("cobblemon:kasib_berry", "cobblemon:kasib_berry")
            put("cobblemon:kebia_berry", "cobblemon:kebia_berry")
            put("cobblemon:kee_berry", "cobblemon:kee_berry")
            put("cobblemon:kelpsy_berry", "cobblemon:kelpsy_berry")
            put("cobblemon:lansat_berry", "cobblemon:lansat_berry")
            put("cobblemon:leppa_berry", "cobblemon:leppa_berry")
            put("cobblemon:liechi_berry", "cobblemon:liechi_berry")
            put("cobblemon:lum_berry", "cobblemon:lum_berry")
            put("cobblemon:lure_berry", "cobblemon:lure_berry")
            put("cobblemon:mago_berry", "cobblemon:mago_berry")
            put("cobblemon:magost_berry", "cobblemon:magost_berry")
            put("cobblemon:maranga_berry", "cobblemon:maranga_berry")
            put("cobblemon:micle_berry", "cobblemon:micle_berry")
            put("cobblemon:nanab_berry", "cobblemon:nanab_berry")
            put("cobblemon:nomel_berry", "cobblemon:nomel_berry")
            put("cobblemon:occa_berry", "cobblemon:occa_berry")
            put("cobblemon:oran_berry", "cobblemon:oran_berry")
            put("cobblemon:pamtre_berry", "cobblemon:pamtre_berry")
            put("cobblemon:passho_berry", "cobblemon:passho_berry")
            put("cobblemon:payapa_berry", "cobblemon:payapa_berry")
            put("cobblemon:pecha_berry", "cobblemon:pecha_berry")
            put("cobblemon:persim_berry", "cobblemon:persim_berry")
            put("cobblemon:petaya_berry", "cobblemon:petaya_berry")
            put("cobblemon:pinap_berry", "cobblemon:pinap_berry")
            put("cobblemon:pomeg_berry", "cobblemon:pomeg_berry")
            put("cobblemon:qualot_berry", "cobblemon:qualot_berry")
            put("cobblemon:rabuta_berry", "cobblemon:rabuta_berry")
            put("cobblemon:rawst_berry", "cobblemon:rawst_berry")
            put("cobblemon:razz_berry", "cobblemon:razz_berry")
            put("cobblemon:rindo_berry", "cobblemon:rindo_berry")
            put("cobblemon:roseli_berry", "cobblemon:roseli_berry")
            put("cobblemon:rowap_berry", "cobblemon:rowap_berry")
            put("cobblemon:salac_berry", "cobblemon:salac_berry")
            put("cobblemon:shuca_berry", "cobblemon:shuca_berry")
            put("cobblemon:sitrus_berry", "cobblemon:sitrus_berry")
            put("cobblemon:spelon_berry", "cobblemon:spelon_berry")
            put("cobblemon:starf_berry", "cobblemon:starf_berry")
            put("cobblemon:tamato_berry", "cobblemon:tamato_berry")
            put("cobblemon:tanga_berry", "cobblemon:tanga_berry")
            put("cobblemon:touga_berry", "cobblemon:touga_berry")
            put("cobblemon:wacan_berry", "cobblemon:wacan_berry")
            put("cobblemon:watmel_berry", "cobblemon:watmel_berry")
            put("cobblemon:wepear_berry", "cobblemon:wepear_berry")
            put("cobblemon:wiki_berry", "cobblemon:wiki_berry")
            put("cobblemon:yache_berry", "cobblemon:yache_berry")

            // Mint seeds → mint leaves (note: plural "seeds")
            put("cobblemon:red_mint_seeds", "cobblemon:red_mint_leaf")
            put("cobblemon:blue_mint_seeds", "cobblemon:blue_mint_leaf")
            put("cobblemon:green_mint_seeds", "cobblemon:green_mint_leaf")
            put("cobblemon:pink_mint_seeds", "cobblemon:pink_mint_leaf")
            put("cobblemon:white_mint_seeds", "cobblemon:white_mint_leaf")
            put("cobblemon:cyan_mint_seeds", "cobblemon:cyan_mint_leaf")
        }

        fun isValidSeed(stack: ItemStack): Boolean {
            val id = BuiltInRegistries.ITEM.getKey(stack.item)?.toString() ?: return false
            return id in ITEM_MAP
        }

        fun isValidUpgrade(stack: ItemStack): Boolean {
            val id = BuiltInRegistries.ITEM.getKey(stack.item)?.toString() ?: return false
            return id == ProcessingChamberItems.OVERCLOCK_CARD_ID.toString() ||
                id == ProcessingChamberItems.FORTUNE_CARD_ID.toString() ||
                id == ProcessingChamberItems.OPTIMIZATION_CARD_ID.toString()
        }

        fun isUpgradeSlot(slot: Int): Boolean = slot in SLOT_UPGRADE_START..SLOT_UPGRADE_END

        val OUTPUT_SLOTS: IntArray = IntArray(SLOT_OUTPUT_END - SLOT_OUTPUT_START + 1) {
            SLOT_OUTPUT_START + it
        }
    }

    var energy: Int = 0
        private set
    var processProgress: Int = 0
    private val items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY)
    private val menuData = object : ContainerData {
        override fun get(index: Int): Int {
            return when (index) {
                DATA_ENERGY -> energy
                DATA_CAPACITY -> capacity
                DATA_PROGRESS -> processProgress
                DATA_PROCESS_TIME -> getModifiedMaxProgress()
                DATA_ENERGY_PER_TICK -> getModifiedEnergyPerTick()
                DATA_FORTUNE_CHANCE_PERCENT -> (getBonusOutputChance() * 100.0f).roundToInt()
                else -> 0
            }
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                DATA_ENERGY -> energy = value
                DATA_PROGRESS -> processProgress = value
            }
        }

        override fun getCount(): Int = DATA_COUNT
    }

    override fun getDisplayName(): Component =
        if (blockState.block is AdvancedProcessingChamberBlock) {
            Component.translatable("container.processing_chamber.advanced_resource_processor")
        } else {
            Component.translatable("container.processing_chamber.resource_processor")
        }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
        return ProcessingChamberMenu(containerId, playerInventory, this, menuData)
    }

    fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        val accepted = minOf(maxReceive, capacity - energy, this.maxReceive)
        if (!simulate) {
            energy += accepted
            setChanged()
        }
        return accepted
    }

    fun extractEnergy(maxExtract: Int, simulate: Boolean): Int = 0

    fun saveEnergyToStack(stack: ItemStack) {
        stack.remove(DataComponents.BLOCK_ENTITY_DATA)
        if (energy <= 0) return
        val tag = CompoundTag()
        tag.putInt(STACK_ENERGY_KEY, energy.coerceIn(0, capacity))
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag))
    }

    fun loadEnergyFromStack(stack: ItemStack) {
        stack.remove(DataComponents.BLOCK_ENTITY_DATA)
        val data = stack.get(DataComponents.CUSTOM_DATA) ?: return
        val tag = data.copyTag()
        if (!tag.contains(STACK_ENERGY_KEY)) return
        energy = tag.getInt(STACK_ENERGY_KEY).coerceIn(0, capacity)
        setChanged()
        syncToClient()
    }

    fun getActiveUpgradeSlotCount(): Int = upgradeSlotCount.coerceIn(0, MAX_UPGRADE_SLOTS)

    fun isUpgradeSlotActive(slot: Int): Boolean =
        isUpgradeSlot(slot) && slot < SLOT_UPGRADE_START + getActiveUpgradeSlotCount()

    fun canInsertUpgrade(stack: ItemStack, slot: Int): Boolean {
        if (!isUpgradeSlotActive(slot)) return false
        if (!isValidUpgrade(stack)) return false
        return !hasSameUpgradeInAnotherSlot(stack, slot)
    }

    private fun hasSameUpgradeInAnotherSlot(stack: ItemStack, slotToIgnore: Int): Boolean {
        for (slot in SLOT_UPGRADE_START..SLOT_UPGRADE_END) {
            if (slot == slotToIgnore) continue
            val other = items[slot]
            if (!other.isEmpty && ItemStack.isSameItemSameComponents(other, stack)) return true
        }
        return false
    }

    private fun hasUpgrade(id: ResourceLocation): Boolean {
        for (slot in SLOT_UPGRADE_START until SLOT_UPGRADE_START + getActiveUpgradeSlotCount()) {
            val stack = items[slot]
            if (!stack.isEmpty && BuiltInRegistries.ITEM.getKey(stack.item)?.toString() == id.toString()) {
                return true
            }
        }
        return false
    }

    fun hasOverclockCard(): Boolean = hasUpgrade(ProcessingChamberItems.OVERCLOCK_CARD_ID)
    fun hasOptimizationCard(): Boolean = hasUpgrade(ProcessingChamberItems.OPTIMIZATION_CARD_ID)
    fun hasFortuneCard(): Boolean = hasUpgrade(ProcessingChamberItems.FORTUNE_CARD_ID)

    fun getModifiedMaxProgress(): Int {
        val baseTime = maxOf(minProcessTime, processTime, 1)
        if (!hasOverclockCard()) return baseTime

        val modifiedTime = baseTime * ProcessingChamberConfig.upgrades.overclockTimeMultiplier
        return maxOf(modifiedTime.roundToInt(), 1)
    }

    fun getModifiedEnergyPerTick(): Int {
        var value = energyPerTick.toFloat()
        if (hasOverclockCard()) value *= ProcessingChamberConfig.upgrades.overclockEnergyMultiplier
        if (hasOptimizationCard()) value *= ProcessingChamberConfig.upgrades.optimizationEnergyMultiplier
        if (hasFortuneCard()) value *= ProcessingChamberConfig.upgrades.fortuneEnergyMultiplier
        return maxOf(ProcessingChamberConfig.upgrades.minEnergyPerTick, value.roundToInt(), 0)
    }

    private fun getBonusOutputChance(): Float {
        if (!hasFortuneCard()) return 0.0f
        return fortuneBonusChance.coerceIn(0.0f, 1.0f)
    }

    fun tick() {
        if (level == null || level!!.isClientSide) return

        transferOutputsDown()

        if (!hasValidRecipe() || !hasOutputSpace()) {
            if (processProgress > 0) {
                processProgress = 0
                setChanged()
            }
            return
        }

        val energyCost = getModifiedEnergyPerTick()
        if (energy >= energyCost) {
            energy -= energyCost
            processProgress++
            setChanged()

            if (processProgress >= getModifiedMaxProgress()) {
                if (process()) {
                    processProgress = 0
                    syncToClient()
                }
            }
        }
    }

    private fun hasValidRecipe(): Boolean {
        val input = items[0]
        if (input.isEmpty) return false
        val result = getResult(input)
        return !result.isEmpty
    }

    private fun hasOutputSpace(): Boolean {
        val result = getResult(items[SLOT_INPUT])
        return !result.isEmpty && findOutputSlot(result) != -1
    }

    private fun process(): Boolean {
        val input = items[0]
        val result = getResult(input)
        if (result.isEmpty) return false

        val outputSlot = findOutputSlot(result)
        if (outputSlot == -1) return false

        val output = items[outputSlot]
        if (output.isEmpty) {
            items[outputSlot] = result.copy()
        } else {
            output.grow(result.count)
        }

        tryProcessBonusOutput(result)
        setChanged()
        return true
    }

    private fun tryProcessBonusOutput(result: ItemStack) {
        val chance = getBonusOutputChance()
        val currentLevel = level ?: return
        if (chance <= 0.0f || currentLevel.random.nextFloat() >= chance) return

        val bonus = result.copy()
        bonus.count = 1
        val outputSlot = findOutputSlot(bonus)
        if (outputSlot == -1) return

        val output = items[outputSlot]
        if (output.isEmpty) {
            items[outputSlot] = bonus
        } else {
            output.grow(1)
        }
    }

    private fun findOutputSlot(result: ItemStack): Int {
        for (slot in SLOT_OUTPUT_START..SLOT_OUTPUT_END) {
            val output = items[slot]
            if (ItemStack.isSameItemSameComponents(output, result) &&
                output.count + result.count <= output.maxStackSize
            ) {
                return slot
            }
        }

        for (slot in SLOT_OUTPUT_START..SLOT_OUTPUT_END) {
            if (items[slot].isEmpty) return slot
        }

        return -1
    }

    private fun getResult(input: ItemStack): ItemStack {
        val inputId = BuiltInRegistries.ITEM.getKey(input.item)?.toString() ?: ""
        val resultId = ITEM_MAP[inputId] ?: return ItemStack.EMPTY
        val item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(resultId))
        if (item == BuiltInRegistries.ITEM.get(ResourceLocation.tryParse("minecraft:air"))) return ItemStack.EMPTY
        return ItemStack(item, outputAmount)
    }

    private fun transferOutputsDown(): Boolean {
        val target = level?.getBlockEntity(worldPosition.below()) as? CapsuleBlockEntity ?: return false
        var movedAny = false

        for (slot in SLOT_OUTPUT_START..SLOT_OUTPUT_END) {
            val source = items[slot]
            if (source.isEmpty) continue

            if (target.acceptOutput(source) > 0) {
                movedAny = true
                if (source.isEmpty) {
                    items[slot] = ItemStack.EMPTY
                }
            }
        }

        if (movedAny) {
            setChanged()
            target.setChanged()
            syncToClient()
            target.syncToClient()
        }

        return movedAny
    }

    private fun acceptOutput(source: ItemStack): Int {
        var moved = 0

        for (slot in SLOT_OUTPUT_START..SLOT_OUTPUT_END) {
            val target = items[slot]
            if (!ItemStack.isSameItemSameComponents(target, source)) continue

            val amount = minOf(source.count, target.maxStackSize - target.count)
            if (amount <= 0) continue

            target.grow(amount)
            source.shrink(amount)
            moved += amount
            if (source.isEmpty) return moved
        }

        for (slot in SLOT_OUTPUT_START..SLOT_OUTPUT_END) {
            if (!items[slot].isEmpty) continue

            val amount = minOf(source.count, source.maxStackSize)
            val inserted = source.copy()
            inserted.count = amount
            items[slot] = inserted
            source.shrink(amount)
            moved += amount
            if (source.isEmpty) return moved
        }

        return moved
    }

    // --- WorldlyContainer ---

    override fun getContainerSize(): Int = CONTAINER_SIZE
    override fun isEmpty(): Boolean = items.all { it.isEmpty }
    override fun getItem(slot: Int): ItemStack = items[slot]
    override fun removeItem(slot: Int, amount: Int): ItemStack {
        val result = ContainerHelper.removeItem(items, slot, amount)
        if (!result.isEmpty) syncToClient()
        return result
    }
    override fun removeItemNoUpdate(slot: Int): ItemStack = ContainerHelper.takeItem(items, slot)
    override fun setItem(slot: Int, stack: ItemStack) {
        if (isUpgradeSlot(slot)) {
            if (stack.isEmpty) {
                items[slot] = ItemStack.EMPTY
            } else if (canInsertUpgrade(stack, slot)) {
                val single = stack.copy()
                single.count = 1
                items[slot] = single
            } else {
                return
            }
        } else {
            items[slot] = stack
        }
        if (stack.count > maxStackSize) stack.count = maxStackSize
        setChanged()
        syncToClient()
    }

    private fun syncToClient() {
        if (level != null && !level!!.isClientSide) {
            val packet = getUpdatePacket()
            for (player in level!!.players()) {
                if (player is ServerPlayer) {
                    player.connection.send(packet)
                }
            }
        }
    }
    override fun stillValid(player: net.minecraft.world.entity.player.Player): Boolean =
        if (level?.getBlockEntity(worldPosition) != this) false
        else player.distanceToSqr(
            worldPosition.x + 0.5, worldPosition.y + 0.5, worldPosition.z + 0.5
        ) <= 64.0
    override fun clearContent() = items.fill(ItemStack.EMPTY)
    override fun getSlotsForFace(side: Direction?): IntArray {
        return if (side == Direction.DOWN) OUTPUT_SLOTS else intArrayOf(SLOT_INPUT)
    }

    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, side: Direction?): Boolean {
        if (slot != SLOT_INPUT || side != Direction.UP) return false
        if (!getItem(0).isEmpty) return false
        if (!isValidSeed(stack)) return false
        val result = getResult(stack)
        if (result.isEmpty) return false
        return findOutputSlot(result) != -1
    }

    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, side: Direction?): Boolean =
        slot in SLOT_OUTPUT_START..SLOT_OUTPUT_END && side == Direction.DOWN

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.putInt("energy", energy)
        tag.putInt("progress", processProgress)
        ContainerHelper.saveAllItems(tag, items, registries)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        energy = tag.getInt("energy").coerceIn(0, capacity)
        items.fill(ItemStack.EMPTY)
        ContainerHelper.loadAllItems(tag, items, registries)
        processProgress = tag.getInt("progress").coerceIn(0, getModifiedMaxProgress())
    }

    fun loadData(tag: CompoundTag, registries: HolderLookup.Provider) {
        loadAdditional(tag, registries)
    }

    override fun getUpdatePacket(): net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithFullMetadata(registries)
    }
}
