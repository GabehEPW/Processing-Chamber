package com.nbp.processing_chamber

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

object ProcessingChamber {
    const val MOD_ID = "processing_chamber"

    val logger = LoggerFactory.getLogger("ProcessingChamber")

    fun init() {
        logger.info("Meu Addon Cobblemon carregado!")

        CobblemonEvents.POKEMON_CAPTURED.subscribe { event ->
            val player = event.player
            val pokemon = event.pokemon

            player.sendSystemMessage(
                Component.literal("Voce capturou: ${pokemon.species.name}")
            )
        }
    }
}
