package com.nbp.apricorn_chamber

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

object ApricornChamber {
    const val MOD_ID = "apricorn_chamber"

    private val logger = LoggerFactory.getLogger("ApricornChamber")

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
