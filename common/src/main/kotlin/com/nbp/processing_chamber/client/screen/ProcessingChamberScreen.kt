package com.nbp.processing_chamber.client.screen

import com.nbp.processing_chamber.ProcessingChamber
import com.nbp.processing_chamber.menu.ProcessingChamberMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import kotlin.math.ceil

class ProcessingChamberScreen(
    menu: ProcessingChamberMenu,
    inventory: Inventory,
    title: Component,
) : AbstractContainerScreen<ProcessingChamberMenu>(menu, inventory, title) {

    init {
        imageWidth = WIDTH
        imageHeight = HEIGHT
        titleLabelY = 8
        inventoryLabelX = 15
        inventoryLabelY = 104
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderMeterTooltip(guiGraphics, mouseX, mouseY)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val left = leftPos
        val top = topPos

        guiGraphics.blit(BACKGROUND, left, top, 0F, 0F, WIDTH, HEIGHT, WIDTH, HEIGHT)
        renderEnergy(guiGraphics, left, top)
        renderProgress(guiGraphics, left, top)
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        val titleX = ((imageWidth - font.width(title)) / 2).coerceAtLeast(0)
        guiGraphics.drawString(font, title, titleX, titleLabelY, 0xFFFFFF, true)
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0xD7D7D7, false)
    }

    private fun renderEnergy(guiGraphics: GuiGraphics, left: Int, top: Int) {
        val frame = if (menu.capacity <= 0) {
            BATTERY_FRAMES - 1
        } else {
            val filledRatio = menu.energy.coerceIn(0, menu.capacity) / menu.capacity.toFloat()
            val filledBars = ceil(filledRatio * (BATTERY_FRAMES - 1)).toInt()
            (BATTERY_FRAMES - 1 - filledBars).coerceIn(0, BATTERY_FRAMES - 1)
        }

        guiGraphics.blit(
            BATTERY,
            left + BATTERY_X,
            top + BATTERY_Y,
            0F,
            (frame * BATTERY_HEIGHT).toFloat(),
            BATTERY_WIDTH,
            BATTERY_HEIGHT,
            BATTERY_WIDTH,
            BATTERY_HEIGHT * BATTERY_FRAMES,
        )
    }

    private fun renderProgress(guiGraphics: GuiGraphics, left: Int, top: Int) {
        val frame = if (menu.processTime <= 0) {
            0
        } else {
            val filledRatio = menu.progress.coerceIn(0, menu.processTime) / menu.processTime.toFloat()
            ceil(filledRatio * (PROGRESS_FRAMES - 1))
                .toInt()
                .coerceIn(0, PROGRESS_FRAMES - 1)
        }

        guiGraphics.blit(
            PROGRESS,
            left + PROGRESS_X,
            top + PROGRESS_Y,
            (frame * PROGRESS_WIDTH).toFloat(),
            0F,
            PROGRESS_WIDTH,
            PROGRESS_HEIGHT,
            PROGRESS_WIDTH * PROGRESS_FRAMES,
            PROGRESS_HEIGHT,
        )
    }

    private fun renderMeterTooltip(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        val localX = mouseX - leftPos
        val localY = mouseY - topPos

        if (localX in BATTERY_X until BATTERY_X + BATTERY_WIDTH &&
            localY in BATTERY_Y until BATTERY_Y + BATTERY_HEIGHT
        ) {
            guiGraphics.renderComponentTooltip(
                font,
                listOf(
                    Component.literal("${menu.energy} / ${menu.capacity} FE"),
                    Component.literal("${menu.energyPerTick} FE/t"),
                ),
                mouseX,
                mouseY,
            )
            return
        }

        if (localX in PROGRESS_X until PROGRESS_X + PROGRESS_WIDTH &&
            localY in PROGRESS_Y until PROGRESS_Y + PROGRESS_HEIGHT
        ) {
            val visibleProgress = menu.progress.coerceIn(0, menu.processTime)
            guiGraphics.renderComponentTooltip(
                font,
                listOf(
                    Component.literal("$visibleProgress / ${menu.processTime}"),
                    Component.literal("${menu.energyPerTick} FE/t"),
                    Component.literal("Fortune: ${menu.fortuneChancePercent}%"),
                ),
                mouseX,
                mouseY,
            )
        }
    }

    companion object {
        private const val WIDTH = 192
        private const val HEIGHT = 198

        private const val BATTERY_X = 20
        private const val BATTERY_Y = 26
        private const val BATTERY_WIDTH = 19
        private const val BATTERY_HEIGHT = 74
        private const val BATTERY_FRAMES = 10

        private const val PROGRESS_X = 71
        private const val PROGRESS_Y = 67
        private const val PROGRESS_WIDTH = 20
        private const val PROGRESS_HEIGHT = 8
        private const val PROGRESS_FRAMES = 7

        private fun resource(path: String): ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(ProcessingChamber.MOD_ID, path)

        private val BACKGROUND = resource("textures/gui/interfaceprocessingchamber.png")
        private val BATTERY = resource("textures/gui/modelobateria.png")
        private val PROGRESS = resource("textures/gui/modelocarregamento.png")
    }
}
