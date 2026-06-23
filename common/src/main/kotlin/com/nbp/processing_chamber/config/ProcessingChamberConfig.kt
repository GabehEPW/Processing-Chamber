package com.nbp.processing_chamber.config

import com.google.gson.JsonParser
import java.nio.file.Files
import java.nio.file.Path

object ProcessingChamberConfig {
    var configDir: Path? = null

    data class TierConfig(
        val energyPerTick: Int = 50,
        val processTime: Int = 100,
        val outputAmount: Int = 1,
    )

    var normal: TierConfig = TierConfig()
        private set
    var advanced: TierConfig = TierConfig(100, 60, 3)
        private set

    private val DEFAULT_JSON = """{
  "normal": {
    "energy_per_tick": 50,
    "process_time": 100,
    "output_amount": 1
  },
  "advanced": {
    "energy_per_tick": 100,
    "process_time": 60,
    "output_amount": 3
  }
}"""

    fun load() {
        val dir = configDir ?: return
        val file = dir.resolve("processing_chamber.json")
        if (Files.exists(file)) {
            try {
                val json = JsonParser.parseReader(Files.newBufferedReader(file)).asJsonObject
                if (json.has("normal")) {
                    val n = json.getAsJsonObject("normal")
                    normal = TierConfig(
                        n.get("energy_per_tick")?.asInt ?: 50,
                        n.get("process_time")?.asInt ?: 100,
                        n.get("output_amount")?.asInt ?: 1,
                    )
                }
                if (json.has("advanced")) {
                    val a = json.getAsJsonObject("advanced")
                    advanced = TierConfig(
                        a.get("energy_per_tick")?.asInt ?: 75,
                        a.get("process_time")?.asInt ?: 60,
                        a.get("output_amount")?.asInt ?: 1,
                    )
                }
            } catch (_: Exception) { }
        } else {
            Files.createDirectories(dir)
            Files.writeString(file, DEFAULT_JSON)
        }
    }
}