# TrHologram
#### 轻便的高级动态全息系统

---
##### Features
- 支持自动导入 HolographicDisplays 全息图
- 跨版本兼容, 代码开源, 持续更新
- 轻量级全息系统, 无需安装额外拓展
- 支持使用 PlaceholderAPI 变量
- 支持自定义全息图的 动态可视范围
- 支持自定义全息图的 动态可视条件
- 支持每行全息内容独立的 可视条件、更新周期
- 支持配置全息图交互动作, 支持 TrMenu 19+ 动作以及参数
  - JavaScript、音效、Title、跨服、Json消息 等等
  - 每个动作还支持设置独立的 延时/概率/条件 等参数
- 更强的物品展示，支持纹理头颅, 染色, 旗帜, NBT 物品 等
- 支持实时编辑, 即时无闪烁重载
- 支持游戏内通过 GUI 管理全息图, 书本编辑全息图等
- 提供完善的 API, 事件
---
##### Usage
- Permission: `trhologram.admin`
- SoftDepend: PlaceholderAPI
- Hologram lines:
  - (Update, ticks) `Example line <update:20>`
  - (Requirement) `Example line <require:player.isOp()>`
---
##### TO-DO List

- 支持物品悬浮，而非局限于套盔甲架头上
- 更完善的全 GUI 管理编辑