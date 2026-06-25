package com.nbp.processing_chamber.item

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class TooltipItem(
    properties: Properties,
    private val tooltipKeys: List<String>,
) : Item(properties) {
    constructor(properties: Properties, tooltipKey: String) : this(properties, listOf(tooltipKey))

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
        for (key in tooltipKeys) {
            tooltipComponents.add(Component.translatable(key).withStyle(ChatFormatting.GRAY))
        }
    }
}
