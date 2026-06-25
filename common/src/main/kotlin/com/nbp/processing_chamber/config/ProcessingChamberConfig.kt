package com.nbp.processing_chamber.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.nio.file.Files
import java.nio.file.Path

object ProcessingChamberConfig {
    var configDir: Path? = null

    data class TierConfig(
        val maxEnergy: Int = 5000,
        val energyPerTick: Int = 15,
        val processTime: Int = 1800,
        val outputAmount: Int = 1,
        val upgradeSlots: Int = 1,
        val minProcessTime: Int = 1200,
        val fortuneBonusChance: Float = 0.15f,
    )

    data class UpgradeConfig(
        val overclockTimeMultiplier: Float = 0.80f,
        val overclockEnergyMultiplier: Float = 1.40f,
        val optimizationEnergyMultiplier: Float = 0.70f,
        val fortuneEnergyMultiplier: Float = 1.20f,
        val minEnergyPerTick: Int = 5,
    )

    var normal: TierConfig = TierConfig()
        private set
    var advanced: TierConfig = TierConfig(10000, 45, 900, 1, 3, 600, 0.25f)
        private set
    var upgrades: UpgradeConfig = UpgradeConfig()
        private set

    private val DEFAULT_JSON = """{
  "normal": {
    "max_energy": 5000,
    "energy_per_tick": 15,
    "process_time": 1800,
    "output_amount": 1,
    "upgrade_slots": 1,
    "min_process_time": 1200,
    "fortune_bonus_chance": 0.15
  },
  "advanced": {
    "max_energy": 10000,
    "energy_per_tick": 45,
    "process_time": 900,
    "output_amount": 1,
    "upgrade_slots": 3,
    "min_process_time": 600,
    "fortune_bonus_chance": 0.25
  },
  "upgrades": {
    "overclock_time_multiplier": 0.8,
    "overclock_energy_multiplier": 1.4,
    "optimization_energy_multiplier": 0.7,
    "fortune_energy_multiplier": 1.2,
    "min_energy_per_tick": 5
  }
}"""

    fun load() {
        val dir = configDir ?: return
        val file = dir.resolve("processing_chamber.json")
        val normalDefaults = TierConfig()
        val advancedDefaults = TierConfig(10000, 45, 900, 1, 3, 600, 0.25f)
        val upgradeDefaults = UpgradeConfig()
        normal = normalDefaults
        advanced = advancedDefaults
        upgrades = upgradeDefaults

        if (Files.exists(file)) {
            try {
                val json = JsonParser.parseReader(Files.newBufferedReader(file)).asJsonObject
                if (json.has("normal")) {
                    val n = json.getAsJsonObject("normal")
                    normal = readTier(n, normalDefaults)
                }
                if (json.has("advanced")) {
                    val a = json.getAsJsonObject("advanced")
                    advanced = readTier(a, advancedDefaults)
                }
                if (json.has("upgrades")) {
                    upgrades = readUpgrades(json.getAsJsonObject("upgrades"), upgradeDefaults)
                }
                writeCurrentConfig(file)
            } catch (_: Exception) { }
        } else {
            Files.createDirectories(dir)
            Files.writeString(file, DEFAULT_JSON)
        }
    }

    private fun readTier(json: JsonObject, defaults: TierConfig): TierConfig {
        return TierConfig(
            json.get("max_energy")?.asInt ?: defaults.maxEnergy,
            json.get("energy_per_tick")?.asInt ?: defaults.energyPerTick,
            json.get("process_time")?.asInt ?: defaults.processTime,
            json.get("output_amount")?.asInt ?: defaults.outputAmount,
            json.get("upgrade_slots")?.asInt ?: defaults.upgradeSlots,
            json.get("min_process_time")?.asInt ?: defaults.minProcessTime,
            readFloat(json, "fortune_bonus_chance", "loot_bonus_chance") ?: defaults.fortuneBonusChance,
        )
    }

    private fun readUpgrades(json: JsonObject, defaults: UpgradeConfig): UpgradeConfig =
        UpgradeConfig(
            normalizeOverclockTimeMultiplier(
                readFloat(json, "overclock_time_multiplier", "speed_time_multiplier") ?: defaults.overclockTimeMultiplier,
                defaults.overclockTimeMultiplier,
            ),
            readFloat(json, "overclock_energy_multiplier", "speed_energy_multiplier") ?: defaults.overclockEnergyMultiplier,
            readFloat(json, "optimization_energy_multiplier", "efficiency_energy_multiplier") ?: defaults.optimizationEnergyMultiplier,
            readFloat(json, "fortune_energy_multiplier", "loot_energy_multiplier") ?: defaults.fortuneEnergyMultiplier,
            json.get("min_energy_per_tick")?.asInt ?: defaults.minEnergyPerTick,
        )

    private fun normalizeOverclockTimeMultiplier(value: Float, default: Float): Float {
        if (value <= 0.0f) return default
        return if (value < 0.1f) value * 10.0f else value
    }

    private fun readFloat(json: JsonObject, key: String, legacyKey: String): Float? =
        json.get(key)?.asFloat ?: json.get(legacyKey)?.asFloat

    private fun writeCurrentConfig(file: Path) {
        val root = JsonObject()
        root.add("normal", writeTier(normal))
        root.add("advanced", writeTier(advanced))
        root.add("upgrades", JsonObject().apply {
            addProperty("overclock_time_multiplier", upgrades.overclockTimeMultiplier)
            addProperty("overclock_energy_multiplier", upgrades.overclockEnergyMultiplier)
            addProperty("optimization_energy_multiplier", upgrades.optimizationEnergyMultiplier)
            addProperty("fortune_energy_multiplier", upgrades.fortuneEnergyMultiplier)
            addProperty("min_energy_per_tick", upgrades.minEnergyPerTick)
        })
        Files.writeString(file, GsonBuilder().setPrettyPrinting().create().toJson(root))
    }

    private fun writeTier(tier: TierConfig): JsonObject =
        JsonObject().apply {
            addProperty("max_energy", tier.maxEnergy)
            addProperty("energy_per_tick", tier.energyPerTick)
            addProperty("process_time", tier.processTime)
            addProperty("output_amount", tier.outputAmount)
            addProperty("upgrade_slots", tier.upgradeSlots)
            addProperty("min_process_time", tier.minProcessTime)
            addProperty("fortune_bonus_chance", tier.fortuneBonusChance)
        }
}
