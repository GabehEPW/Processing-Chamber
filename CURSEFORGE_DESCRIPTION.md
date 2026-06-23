# Processing Chamber

**Processing Chamber** is a **Cobblemon** addon that adds energy-powered machines to process apricorn seeds, berries, and mint seeds into their mature forms — fully automatable with hoppers and item pipes.

---

## 🔧 Content

### Blocks

| Block | Description |
|---|---|
| **Processing Chamber** | Basic machine — processes 1 item per craft, consumes 50 FE/t, takes 100 ticks (5 seconds) |
| **Processing Chamber Advanced** | Advanced machine — processes **3 items per craft**, consumes 100 FE/t, takes only 60 ticks (3 seconds) |

### Item

| Item | Description |
|---|---|
| **Processing Chamber Upgrade** | Right-click a basic chamber to perform an **instant in-place upgrade** to the advanced version, preserving all progress, energy, and stored items |

---

## ⚙️ How It Works

1. Place the **Processing Chamber** in the world
2. Supply energy through the **back face** (opposite to the direction the machine is facing)
3. Insert seeds or berries manually (right-click) or via **hopper from the top**
4. The machine processes automatically and deposits the result in the output slot
5. Collect the result manually or via **hopper from the bottom**

### In-Place Upgrade
Hold the **Processing Chamber Upgrade** and right-click the basic chamber to instantly convert it to the advanced version. All energy, progress, and items are preserved.

---

## 📋 Supported Recipes (77 total)

### Apricorn Seeds → Apricorns (7)
| Input | Output |
|---|---|
| Black Apricorn Seed | Black Apricorn |
| Blue Apricorn Seed | Blue Apricorn |
| Green Apricorn Seed | Green Apricorn |
| Pink Apricorn Seed | Pink Apricorn |
| Red Apricorn Seed | Red Apricorn |
| White Apricorn Seed | White Apricorn |
| Yellow Apricorn Seed | Yellow Apricorn |

### Berries → Same Berry (64)
All Cobblemon berries are processed and output **one mature berry** (same type). Perfect for automated berry multiplication!

Full list: aguav, apicot, aspear, babiri, belue, bluk, charti, cheri, chesto, chilan, chople, coba, colbur, cornn, custap, durin, eggant, enigma, figy, ganlon, grepa, haban, hondew, hopo, iapapa, jaboca, kasib, kebia, kee, kelpsy, lansat, leppa, liechi, lum, lure, mago, magost, maranga, micle, nanab, nomel, occa, oran, pamtre, passho, payapa, pecha, persim, petaya, pinap, pomeg, qualot, rabuta, rawst, razz, rindo, roseli, rowap, salac, shuca, sitrus, spelon, starf, tamato, tanga, touga, wacan, watmel, wepear, wiki, yache

### Mint Seeds → Mint Leaves (6)
| Input | Output |
|---|---|
| Red Mint Seeds | Red Mint Leaf |
| Blue Mint Seeds | Blue Mint Leaf |
| Green Mint Seeds | Green Mint Leaf |
| Pink Mint Seeds | Pink Mint Leaf |
| White Mint Seeds | White Mint Leaf |
| Cyan Mint Seeds | Cyan Mint Leaf |

---

## ⚡ Energy

The machine runs on **Forge Energy (FE)**.

| Parameter | Processing Chamber | Processing Chamber Advanced |
|---|---|---|
| Consumption per tick | 50 FE | 100 FE |
| Processing time | 100 ticks (5s) | 60 ticks (3s) |
| Items produced per craft | 1 | 3 |
| Internal buffer | 200 FE | 400 FE |
| Max receive rate | 200 FE/t | 400 FE/t |

Energy input is **exclusively through the back face** (opposite to the facing direction).

---

## 🔨 Crafting Recipes

### Processing Chamber
```
I I I
G R G
I I I
```
- I: Iron Ingot
- G: Glass
- R: Redstone

### Processing Chamber Upgrade
```
O R O
Q O Q
O R O
```
- O: Obsidian
- R: Redstone
- Q: Quartz

### Processing Chamber Advanced
```
O R O
Q P Q
O R O
```
- O: Obsidian
- R: Redstone
- Q: Quartz
- P: Processing Chamber

---

## ⬇️ Automation

- **Item input**: accepted via hopper on the **top face** (input slot), valid items only
- **Item extraction**: pulled via hopper on the **bottom face** (output slot)
- **Energy**: accepted from any FE generator through the **back face**

---

## 🎨 Visual

Each block has a **detailed 3D model** with a glass window showing the interior. The item being processed **floats and rotates** inside the machine.

Energy bar textures show 6 visual levels (0 to 5) for easy monitoring in the GUI.

---

## 🌐 Languages

- English
- Português (Brasil)

---

## 📦 Dependencies

- **Minecraft 1.21.1**
- **Cobblemon 1.7.0+1.21.1**

### Fabric
- Fabric Loader ≥0.17.0
- Fabric API 0.116.6+
- Fabric Language Kotlin 1.13.3+
- TeamReborn Energy 4.1.0+

### NeoForge
- NeoForge ≥21.1
- Kotlin for Forge 5.9.0+

---

## ⚙️ Configuration

Auto-generated file at `config/processing_chamber.json`:

```json
{
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
}
```

---

## 📜 License

MIT License — Copyright (c) 2026 GabehEPW
