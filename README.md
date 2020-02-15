# TrHologram
#### 轻便的高级动态全息系统

---
##### 特点
- 支持自动导入 HolographicDisplays 全息图
- 跨版本兼容, 代码开源, 持续更新
- 轻量级全息系统, 无需安装额外拓展
- 完全基于数据包，低资源占用，服务端不会产生额外实体
- 支持使用 PlaceholderAPI 变量
- 支持自定义全息图的 **动态可视范围**
- 支持自定义全息图的 **动态可视条件**
- 支持每行全息内容独立的 **可视条件**、**更新周期**
- 支持配置全息图多组优先级的交互动作, 支持 TrMenu 的动作以及参数
  - JavaScript、音效、Title、跨服、Json消息 等等
  - 每个动作还支持设置独立的 延时/概率/条件 等参数
- 更强的物品展示，支持纹理头颅, 染色, 旗帜, NBT 物品 等
- 支持实时编辑, 自动重载，即时无闪烁更新
- 支持游戏内通过 GUI 管理全息图, 书本编辑全息图等
- 完善的命令 TAB 补全
- 提供完善的 API, 事件
---
##### 用法
> 唯一管理权限: `trhologram.admin`
>
> 软依赖: PlaceholderAPI

- 命令（/TrHologram）：
  - 别称：tholo, trhd, hd, holo, hologram
  - 子命令
    - Create
      - 描述：引导创建一个全息图，也可直接提供全息图名称
    - Delete
      - 描述：引导删除一个全息图，也可直接提供全息图名称
    - Edit
      - 描述：引导编辑管理一个全息图，也可以直接提供全息图名称
    - Reload
      - 描述：手动重新载入所有全息图
    - List
      - 描述：列出所有全息图，或列出名称包含过滤参数的全息图
    - Debug
      - 描述：调试信息

- 全息图配置注解
  - 当你创建全息图后，会存储在 `plugins/TrHologram/holograms/<id>.yml` 中
  - 示例配置如下
    - ```YAML
      # (可选) 全息图的视距
      viewDistance: 20.0
      # (可选) 全息图的可视条件, 写表达式
      viewCondition: 'null'
      # 全息图首行的初始坐标位置, 可编辑
      location: world,-463.0,70.0,235.0
      # 全息图的内容
      contents:
      - 'Example Line'
      - 'Line2, with placeholder %player_name%'
      # 全息图的动作组, 后面会详讲  
      actions:
      - 'tell: You clicked this hologram!'
      ```
- 全息内容行用法
  - 内容行都放在 `contents` 节点下, 如果是空行将会隐藏盔甲架名称
  - 你可以使用 PlaceholderAPI 的变量，但默认只加载一次
  - **更新周期**
    - 当某行需要更新变量时，你可以使用该参数指定一个周期
    - 参数: `<update:周期>`, 单位为 ticks
    - 例如：`Hello, Your balance: %vault_eco_balance%<update:20>`
  - **可视条件**
    - 该条件是该行的独立可视条件，玩家需要先满足整个全息图的可视条件
    - 参数: `<require:表达式>`
    - 例如：`NMSL<require:"%player_name%" == "XXX">`
    - > 注意，单行不可视时只会隐藏盔甲架名称，其它行不会自动补位

- 全息交互动作用法
  - 使用之前，你需要知道的是
    - TrHologram 内置 TrMenu 的大多数动作 (除与菜单相关的外)
    - 并且包括动作参数在内，你都可以使用
    - 了解动作：https://trmenu.trixey.cn/v/chinese/actions
  - 第一种，最简单的 List 形式，执行一组动作，如
    ```YAML
    actions:
    - 'tell: &2&lTr&a&lHologram'
    - 'tell: 太牛逼了'
    - 'title: <TITLE=&a&l你觉得呢？><SUBTITLE=&3%player_name%>'
    ```
  - 第一种，多组优先级动作
    > 优先级越大越先判断
    ```YAML
    actions:
      - condition: 'player.hasPermission("user.stupid")'
        priority: 3 #优先级数值
        list: # 动作组列表
        - 'tell: you are stupid'
      - condition: 'player.hasPermission("user.default")'
        priority: 2
        list:
        - 'tell: you are nobody'
        - 'console: give %player_name% diamond 1<chance:0.8>'
    ```

- 配置文件 (Settings.yml)
  ```YAML
  # 不要更改
  CONFIG-VERSION: 0
  
  # 本地语言加载顺序
  LOCALE-PRIORITY:
    - zh_CN
    - en_US
  
  # 选项
  OPTIONS:
    # 自动重载是否播放音效
    AUTO-RELOAD-SOUND: true
    # 每行全息图的盔甲架高度距离
    ARMORSTAND-DISTANCE: 0.25
  
  # 菜单自定义显示物品
  GUIS:
    EDITOR:
      CONTENTS:
        material: WRITABLE_BOOK
        name: '&3编辑内容'
        lore:
          - ''
          - '&7点击按钮编辑该全息图的内容'
          - ''
      MOVE:
        material: ENDER_PEARL
        name: '&2移动位置'
        lore:
          - ''
          - '&7点击移动该全息图到你当前的位置'
          - ''
      DELETE:
        material: LAVA_BUCKET
        name: '&6删除'
        lore:
          - ''
          - '&8[&6&l!&8] &c该操作不可逆，永久删除'
          - '&7点击即可删除该全息图'
          - ''
  ```
---
##### TO-DO List

- 支持物品悬浮，而非局限于套盔甲架头上
- 更完善的全 GUI 管理编辑