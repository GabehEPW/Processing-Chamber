![Banner]([https://i.imgur.com/IXpcGvZ.png](https://imgur.com/a/e2bkDaB))

# Processing Chamber

**Processing Chamber** is a **Cobblemon addon** that adds FE-powered machines for automating the production of **Apricorns**, **Berries**, and **Mint Leaves**.

Build compact processing setups, power your machines with FE, install upgrade cards, and automate resource production using hoppers, item pipes, and energy networks.

---

## ✨ Features

* FE-powered processing machines
* Apricorn Seed automation
* Berry multiplication
* Mint Leaf production
* Upgrade cards with unique effects
* Advanced machine tier
* Custom GUI
* Energy and progress display
* Hopper and pipe automation support
* Configurable machine values
* English and Brazilian Portuguese translations

---

## 🔧 Blocks

| Block                           | Description                                                                                                                                              |
| ------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Resource Processor**          | Basic machine for processing Cobblemon resources. Stores **5,000 FE**, consumes **15 FE/t**, and accepts **1 upgrade card**.                             |
| **Advanced Resource Processor** | Advanced machine with higher energy capacity, faster processing, and up to **3 different upgrade cards**. Stores **10,000 FE** and consumes **45 FE/t**. |

---

## 🧩 Items

| Item                      | Description                                                                                                               |
| ------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| **Processor Upgrade Kit** | Upgrades a Resource Processor into an Advanced Resource Processor while preserving energy, progress, items, and upgrades. |
| **Overclock Card**        | Reduces processing time, but increases FE usage.                                                                          |
| **Optimized Card**        | Reduces FE consumption without changing processing time.                                                                  |
| **Fortune Card**          | Adds a chance to generate bonus output, but increases FE usage.                                                           |

---

## ⚙️ How It Works

1. Place a **Resource Processor** or **Advanced Resource Processor** in the world.
2. Supply FE energy through the **back face** of the machine.
3. Insert a valid Cobblemon item into the input slot.
4. The machine processes automatically while it has energy and output space.
5. Generated items are stored in the **9 output slots**.
6. Outputs can be collected manually or extracted through automation.

The input item is **not consumed every cycle**.

It stays inside the machine and continues producing resources until:

* The output slots are full
* The machine runs out of energy
* The player removes the input item

If the machine runs out of energy, the processing progress is paused instead of being reset.

---

## ⬆️ Processor Upgrade Kit

The **Processor Upgrade Kit** instantly converts a **Resource Processor** into an **Advanced Resource Processor**.

The upgrade preserves:

* Stored energy
* Processing progress
* Input item
* Output items
* Upgrade cards

---

## 🃏 Upgrade Cards

Upgrade cards can only be inserted into upgrade slots.

### Upgrade Rules

* **Resource Processor** accepts **1 upgrade card**
* **Advanced Resource Processor** accepts up to **3 different upgrade cards**
* Duplicate upgrade cards are not allowed in the same machine
* Each upgrade slot holds only **1 card**

---

### Overclock Card

Increases machine speed.

**Effects:**

* Reduces processing time by **20%**
* Increases FE consumption by **40%**

---

### Optimized Card

Improves energy efficiency.

**Effects:**

* Reduces FE consumption by **30%**
* Does not change processing time

---

### Fortune Card

Adds a chance to generate bonus output.

**Effects:**

* **+15%** bonus output chance in the Resource Processor
* **+25%** bonus output chance in the Advanced Resource Processor
* Increases FE consumption by **20%**

---

## 📋 Supported Processing

### Apricorn Seeds → Apricorns

| Input                | Output          |
| -------------------- | --------------- |
| Black Apricorn Seed  | Black Apricorn  |
| Blue Apricorn Seed   | Blue Apricorn   |
| Green Apricorn Seed  | Green Apricorn  |
| Pink Apricorn Seed   | Pink Apricorn   |
| Red Apricorn Seed    | Red Apricorn    |
| White Apricorn Seed  | White Apricorn  |
| Yellow Apricorn Seed | Yellow Apricorn |

---

### Berries → Same Berry

Supported Cobblemon berries can be processed into the same berry type, making berry multiplication easy to automate.

Example:

| Input        | Output       |
| ------------ | ------------ |
| Oran Berry   | Oran Berry   |
| Sitrus Berry | Sitrus Berry |
| Pecha Berry  | Pecha Berry  |

---

### Mint Seeds → Mint Leaves

| Input            | Output          |
| ---------------- | --------------- |
| Red Mint Seeds   | Red Mint Leaf   |
| Blue Mint Seeds  | Blue Mint Leaf  |
| Green Mint Seeds | Green Mint Leaf |
| Pink Mint Seeds  | Pink Mint Leaf  |
| White Mint Seeds | White Mint Leaf |
| Cyan Mint Seeds  | Cyan Mint Leaf  |

---

## ⚡ Energy

Processing Chamber machines use **FE energy**.

| Parameter            | Resource Processor | Advanced Resource Processor |
| -------------------- | -----------------: | --------------------------: |
| Energy storage       |           5,000 FE |                   10,000 FE |
| Base FE consumption  |            15 FE/t |                     45 FE/t |
| Base processing time |  1,800 ticks / 90s |             900 ticks / 45s |
| Output slots         |                  9 |                           9 |
| Upgrade slots        |                  1 |                           3 |

Energy is inserted through the **back face** of the machine.

Stored energy is preserved when the machine is broken and placed again.

---

## 🔨 Crafting Recipes

### Resource Processor

```txt
I I I
G R G
I I I
```

```txt
I: Iron Ingot
G: Glass
R: Redstone
```

---

### Advanced Resource Processor

```txt
O R O
Q P Q
O R O
```

```txt
O: Obsidian
R: Redstone
Q: Quartz
P: Resource Processor
```

---

### Processor Upgrade Kit

```txt
O R O
Q O Q
O R O
```

```txt
O: Obsidian
R: Redstone
Q: Quartz
```

---

### Overclock Card

```txt
R S R
G C G
R S R
```

```txt
R: Redstone
S: Sugar
G: Gold Ingot
C: Copper Ingot
```

---

### Optimized Card

```txt
C L C
R G R
C L C
```

```txt
C: Copper Ingot
L: Lapis Lazuli
R: Redstone
G: Glass Pane
```

---

### Fortune Card

```txt
A Y A
N R N
A Y A
```

```txt
A: Any Cobblemon Apricorn
Y: Any Cobblemon Berry
N: Gold Nugget
R: Redstone
```

---

## ⬇️ Automation

Processing Chamber machines support automation with hoppers, item pipes, and energy networks.

| Side   | Function                        |
| ------ | ------------------------------- |
| Top    | Inserts valid input items       |
| Bottom | Extracts generated output items |
| Back   | Accepts FE energy               |

Machines can also transfer outputs downward into another processor placed below them.

This allows players to build compact vertical processing arrays.

---

## 🎨 Visuals

The machines include:

* Detailed 3D block models
* Custom GUI
* Animated internal item rendering
* Visual energy bar
* Visual progress bar
* Colored machine and upgrade names
* Tooltips showing useful machine information

Tooltips may display:

* Stored energy
* Processing progress
* FE/t usage
* Fortune bonus chance
* Installed upgrade effects

---

## ⚙️ Configuration

The mod creates a configuration file at:

```txt
config/processing_chamber.json
```

Server owners and players can configure:

* Max energy storage
* FE consumption per tick
* Processing time
* Output amount
* Upgrade slot count
* Minimum processing time
* Fortune bonus chance
* Upgrade card multipliers
* Minimum FE/t usage

Example configuration:

```json
{
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
}
```

---

## 🌐 Languages

Processing Chamber currently supports:

* English
* Português do Brasil

---

## 📦 Dependencies

### Common

* Minecraft **1.21.1**
* Cobblemon

### Fabric

* Fabric Loader
* Fabric API
* Fabric Language Kotlin
* TeamReborn Energy

### NeoForge

* NeoForge
* Kotlin for Forge

---

## 📜 License

This project is licensed under the **MIT License**.

Copyright © 2026 **GabehEPW**
