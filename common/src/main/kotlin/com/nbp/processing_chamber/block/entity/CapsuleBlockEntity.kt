package com.nbp.processing_chamber.block.entity

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
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class CapsuleBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type, pos, state), WorldlyContainer {

    companion object {
        const val ENERGY_PER_TICK = 50
        const val CAPACITY = ENERGY_PER_TICK
        const val MAX_RECEIVE = ENERGY_PER_TICK
        const val PROCESS_TIME = 100

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
    }

    var energy: Int = 0
        private set
    var processProgress: Int = 0
    private val items = NonNullList.withSize(2, ItemStack.EMPTY)

    fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        val accepted = minOf(maxReceive, CAPACITY - energy, MAX_RECEIVE)
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
                    syncToClient()
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

        val output = items[1]
        if (output.isEmpty) {
            items[1] = result.copy()
        } else {
            output.grow(result.count)
        }
    }

    private fun getResult(input: ItemStack): ItemStack {
        val inputId = BuiltInRegistries.ITEM.getKey(input.item)?.toString() ?: ""
        val resultId = ITEM_MAP[inputId] ?: return ItemStack.EMPTY
        val item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(resultId))
        if (item == BuiltInRegistries.ITEM.get(ResourceLocation.tryParse("minecraft:air"))) return ItemStack.EMPTY
        return ItemStack(item, 1)
    }

    // --- WorldlyContainer ---

    override fun getContainerSize(): Int = 2
    override fun isEmpty(): Boolean = items.all { it.isEmpty }
    override fun getItem(slot: Int): ItemStack = items[slot]
    override fun removeItem(slot: Int, amount: Int): ItemStack {
        val result = ContainerHelper.removeItem(items, slot, amount)
        if (!result.isEmpty) syncToClient()
        return result
    }
    override fun removeItemNoUpdate(slot: Int): ItemStack = ContainerHelper.takeItem(items, slot)
    override fun setItem(slot: Int, stack: ItemStack) {
        items[slot] = stack
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
    override fun getSlotsForFace(side: Direction?): IntArray = intArrayOf(0, 1)
    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, side: Direction?): Boolean {
        if (slot != 0 || side != Direction.UP) return false
        if (!getItem(0).isEmpty) return false
        if (!isValidSeed(stack)) return false
        val result = getResult(stack)
        if (result.isEmpty) return false
        val output = getItem(1)
        if (output.isEmpty) return true
        return ItemStack.isSameItemSameComponents(output, result) &&
            output.count + result.count <= output.maxStackSize
    }
    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, side: Direction?): Boolean =
        slot == 1 && side == Direction.DOWN

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
        items.fill(ItemStack.EMPTY)
        ContainerHelper.loadAllItems(tag, items, registries)
    }

    override fun getUpdatePacket(): net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithFullMetadata(registries)
    }
}
