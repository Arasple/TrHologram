# TrHologram

#### Incredible Dynamic Hologram-Plugin

---

### Features

- **Performance**
    - 100% Packet-based hologram (armorstand, item), no-lag
    - Async update tasks

- **Advance**
    - Individual update task for each component
    - View-Distance & View-Condition & Custom offset
    - Floating item display support
    - Touchable hologram with custom reactions
    - Functions, PlaceholderAPI support
  
- **API**
    - Friendly use developer API, create dynamic hologram easily

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