# TrHologram

#### Modern & Lightweight Holographic-Plugin
![](https://img.shields.io/github/last-commit/Arasple/TrHologram?logo=artstation&style=for-the-badge&color=9266CC)![](https://img.shields.io/github/issues/Arasple/TrHologram?style=for-the-badge&logo=slashdot)![](https://img.shields.io/github/release/Arasple/TrHologram?style=for-the-badge&color=00C58E&logo=ionic)

---

![bStats](https://bstats.org/signatures/bukkit/TrHologram.svg)

---

### Features

- **Highly Optimized**
    - 100% Packet-based hologram (armorstand, item), no-lag
    - Async update tasks

- **Light & Powerful**
    - Individual update task for each line
    - Custom view distance & view condition
    - Custom line spacing and offset for individual line  
    - Support to display floating item with custom texture
    - Interactive holograms (4 clicktypes integrated)
    - PlaceholderAPI and custom functions support

- **API**
    - Friendly developer API, create dynamic holograms easily

---

### API

Usage

```java
class Demo {

    public void display(Player viewer) {
        Hologram hologram = TrHologramAPI
                .builder(viewer.getLocation())
                .append("Hello World")
                .append(player -> player.getInventory().getItemInMainHand(), 40)
                .interspace(0.5)
                .append("Time: %server_time_ss%", 20)
                .build();

        hologram.refreshVisibility(viewer);

        TextHologram line = hologram.getTextLine(0);
        line.setText("Hello TrHologram");
    }

}
```