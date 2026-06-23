# Processing Chamber

**Processing Chamber** é um addon para **Cobblemon** que adiciona máquinas movidas a energia para processar sementes de apricorn, berries e sementes de mint em suas formas maduras — tudo automatizável com hoppers e pipes.

---

## 🔧 Conteúdo

### Blocos

| Bloco | Descrição |
|---|---|
| **Processing Chamber** | Máquina básica — processa 1 item por craft, consome 50 FE/t, leva 100 ticks (5 segundos) |
| **Processing Chamber Advanced** | Máquina avançada — processa **3 itens por craft**, consome 100 FE/t, leva apenas 60 ticks (3 segundos) |

### Item

| Item | Descrição |
|---|---|
| **Processing Chamber Upgrade** | Usado com clique direito na câmara básica para **upgrade imediato** para a versão avançada, preservando todo o progresso, energia e itens armazenados |

---

## ⚙️ Como Funciona

1. Coloque a **Processing Chamber** no mundo
2. Forneça energia pela **face traseira** (oposta à direção que a máquina está virada)
3. Insira sementes ou berries manualmente (clique direito) ou via **hopper pelo topo**
4. A máquina processa automaticamente e deposita o resultado no slot de saída
5. Retire o resultado manualmente ou via **hopper por baixo**

### Upgrade in-place
Com o **Upgrade Processing Chamber** em mãos, clique com direito na câmara básica para convertê-la imediatamente em avançada. Toda energia, progresso e itens são preservados.

---

## 📋 Receitas Suportadas (77 no total)

### Apricorn Seeds → Apricorns (7)
| Entrada | Saída |
|---|---|
| Black Apricorn Seed | Black Apricorn |
| Blue Apricorn Seed | Blue Apricorn |
| Green Apricorn Seed | Green Apricorn |
| Pink Apricorn Seed | Pink Apricorn |
| Red Apricorn Seed | Red Apricorn |
| White Apricorn Seed | White Apricorn |
| Yellow Apricorn Seed | Yellow Apricorn |

### Berries → Mesma Berry (64)
Todas as berries do Cobblemon são processadas e retornam **uma berry madura** (mesmo tipo). Ideal para multiplicar berries de forma automatizada!

Lista completa: aguav, apicot, aspear, babiri, belue, bluk, charti, cheri, chesto, chilan, chople, coba, colbur, cornn, custap, durin, eggant, enigma, figy, ganlon, grepa, haban, hondew, hopo, iapapa, jaboca, kasib, kebia, kee, kelpsy, lansat, leppa, liechi, lum, lure, mago, magost, maranga, micle, nanab, nomel, occa, oran, pamtre, passho, payapa, pecha, persim, petaya, pinap, pomeg, qualot, rabuta, rawst, razz, rindo, roseli, rowap, salac, shuca, sitrus, spelon, starf, tamato, tanga, touga, wacan, watmel, wepear, wiki, yache

### Mint Seeds → Mint Leaves (6)
| Entrada | Saída |
|---|---|
| Red Mint Seeds | Red Mint Leaf |
| Blue Mint Seeds | Blue Mint Leaf |
| Green Mint Seeds | Green Mint Leaf |
| Pink Mint Seeds | Pink Mint Leaf |
| White Mint Seeds | White Mint Leaf |
| Cyan Mint Seeds | Cyan Mint Leaf |

---

## ⚡ Energia

A máquina utiliza **Forge Energy (FE)**.

| Parâmetro | Processing Chamber | Processing Chamber Advanced |
|---|---|---|
| Consumo por tick | 50 FE | 100 FE |
| Tempo de processamento | 100 ticks (5s) | 60 ticks (3s) |
| Itens produzidos por craft | 1 | 3 |
| Buffer interno | 200 FE | 400 FE |
| Taxa máxima de recebimento | 200 FE/t | 400 FE/t |

A entrada de energia é **exclusivamente pela face traseira** da máquina (oposta à direção em que ela está virada).

---

## 🔨 Receitas de Craft

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

## ⬇️ Automação

- **Entrada de itens**: accepteda via hopper na **face superior** (slot de input), apenas itens válidos
- **Extraçao de itens**: retirada via hopper na **face inferior** (slot de output)
- **Energia**: aceita de qualquer gerador FE pela **face traseira**

---

## 🎨 Visual

Cada bloco possui um **modelo 3D detalhado** com vidro frontal mostrando o interior. O item sendo processado **flutua e gira** dentro da máquina.

As barras de energia no GUI mostram 6 níveis visuais (0 a 5) para fácil monitoramento.

---

## 🌐 Idiomas

- English
- Português (Brasil)

---

## 📦 Dependências

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

## ⚙️ Configuração

Arquivo gerado automaticamente em `config/processing_chamber.json`:

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

## 📜 Licença

MIT License — Copyright (c) 2026 GabehEPW
