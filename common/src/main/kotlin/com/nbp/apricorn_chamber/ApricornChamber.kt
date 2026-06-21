package com.nbp.cobblemon_bands

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

object CobblemonBands {
    const val MOD_ID = "cobblemon_bands"

    private val logger = LoggerFactory.getLogger("CobblemonBands")

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
