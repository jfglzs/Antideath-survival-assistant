# AntiDeath Survival Assistant

![mod icon](res/icon.png)

[![Modrinth](https://img.shields.io/modrinth/dt/Antideath-survival-assistant?color=00AF5C&label=Modrinth%20downloads&logo=modrinth)](https://modrinth.com/mod/Antideath-survival-assistant)
[![GitHub downloads](https://img.shields.io/github/downloads/jfglzs/Antideath-survival-assistant/total?color=161616&label=GitHub%20downloads&logo=github)](https://github.com/jfglzs/Antideath-survival-assistant/releases)
[![License](https://img.shields.io/badge/license-CC0--1.0-blue.svg)](https://github.com/jfglzs/Antideath-survival-assistant/blob/master/LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x%20%7C%2026.x-00AF5C)](https://modrinth.com/mod/Antideath-survival-assistant/versions)

**ASA**（AntiDeath Survival Assistant）是一个客户端 Fabric 模组，为 [masa 系模组](https://masa.dy.fi/) 生态补充生存辅助、界面调整与性能优化功能。

- 基于 **malilib** 的配置界面与快捷键系统，所有功能都可在游戏内调整。
- 覆盖苦力怕预警、假人管理、投影取货、背包整理、界面清理与渲染优化等场景。
- 客户端模组：服务器无需安装 ASA；但假人、取货等功能需要服务端支持 `/player` 等命令。

> 感谢 [@OptiJava](https://github.com/OptiJava) 的指导。

## 目录

- [简介](#intro)
- [前置模组](#dependencies)
- [支持版本](#versions)
- [安装与更新](#install)
- [快速开始](#quickstart)
- [配置分类总览](#categories)
- [功能详解](#features)
  - [一、功能（FUNCTIONS）](#functions)
  - [二、禁用（DISABLES）](#disables)
  - [三、优化（OPTIMIZATIONS）](#optimizations)
  - [四、命令（COMMANDS）](#commands)
  - [五、列表（LISTS）](#lists)
  - [六、LMS 假人取货（LMS）](#lms)
- [配置文件与键名](#config-file)
- [兼容性与注意事项](#compatibility)
- [常见问题](#faq)
- [从源码构建](#build)
- [致谢与许可](#credits)

<a id="dependencies"></a>
## 前置模组

### 必需

| 名称 | 说明 | 下载 |
| --- | --- | --- |
| **malilib** | 配置界面、快捷键与大量工具类；版本必须与游戏版本匹配 | [Sakura Fork（1.21+）](https://github.com/sakura-ryoko/malilib) / [Masa 原版（1.21-）](https://masa.dy.fi/tmp/minecraft/mods/malilib/) |
| **Fabric API** | Fabric 模组基础 API | [MC 百科](https://www.mcmod.cn/class/3124.html) / [官方网站](https://fabricmc.net/) |

### 可选

以下模组不是启动所必需的，但对应的功能需要它们才能工作。

| 名称 | 涉及功能 | 说明 |
| --- | --- | --- |
| **Litematica** | 投影材料列表、中键取货、投影方块替换、材料统计 | 投影相关功能的基础 |
| **MiniHUD** | 挂载 Logger 信息、MiniHUD 掉帧优化 | 需要先在 MiniHUD 中启用 HUD |
| **Tweakeroo** | 自动盒子补货、强制方块挖掘冷却 | 自动补货开关由 Tweakeroo 提供 |
| **ItemScroller** | 自动垃圾清理、潜影盒物品分离器 | 大量背包操作依赖其工具方法 |
| **QuickShulker** | 自动盒子补货、潜影盒物品分离器 | 用于自动打开潜影盒 |
| **Mod Menu** | 在模组列表中显示 ASA 配置入口 | 可选，方便打开设置 |

### 服务端支持

假人与取货相关功能需要服务端具备相应命令或服务端模组。

| 名称 | 涉及功能 | 说明 |
| --- | --- | --- |
| **LMS Carpet Addition** | 假人远程取货、中键取货、假人背包缓存、材料统计 | 提供 `getItem`、`getStorageData` 等命令 |
| **Carpet / Carpet 衍生端** | `/pm`、`/autovault`、假人杀戮光环 | 需要服务端允许 `/player` 命令 |
| **Antideath-carpet-addition** | 修复取货数据中的 NBT 折叠问题 | 需开启 `fixNbtFold` 规则；或使用 LMS 1.14.1+ |

<a id="versions"></a>
## 支持版本

| 构建分支 | 支持的 Minecraft | Java | 状态 |
| --- | --- | --- | --- |
| 1.21.1 | 1.21 – 1.21.3 | 21 | 持续更新 |
| 1.21.4 | 1.21.4 | 21 | 持续更新 |
| 1.21.5 | 1.21.5 | 21 | 持续更新 |
| 1.21.8 | 1.21.6 – 1.21.8 | 21 | 持续更新 |
| 1.21.10 | 1.21.9 – 1.21.10 | 21 | 持续更新 |
| 1.21.11 | 1.21.11 | 21 | 持续更新 |
| 26.1 | 26.1.x | 25 | 持续更新 |
| 26.2 | 26.2 | 25 | 持续更新 |
| 26.3 | 26.3 | 25 | 持续更新 |

<a id="features"></a>
## 功能详解

以下按配置界面的分类整理。每项包含功能说明、类型、默认值、范围/选项、配置键与所属分类；配置键即 `config/asa.json` 中 `asa` 对象下的字段名。

<a id="functions"></a>
### 一、功能（FUNCTIONS）

共 45 项，按用途分为 7 组。

#### 界面与提示

##### 打开设置菜单

打开 ASA 的设置界面，效果等同于从 Mod Menu 进入本模组配置。

- 类型：`快捷键`
- 默认值：`Z,K`
- 配置键：`openGui`
- 分类：`ALL`，`FUNCTIONS`

> `Z,K` 是 malilib 的组合键格式。若与其他模组冲突，可在配置界面或 malilib 快捷键设置中重新绑定。

##### 剩余物品显示Overlay

在屏幕左上角显示**当前主手物品**的图标、名称与背包内的剩余数量。统计范围包括背包、快捷栏以及潜影盒内的同类物品。

![剩余物品显示](res/remain_item_overlay.png)

可在配置中调整 X/Y 偏移。

![X/Y 偏移设置](res/remain_item_overlayXY.png)

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`remainItemOverlay`
- 分类：`ALL`，`FUNCTIONS`

> 显示内容以主手物品为准；主手为空时不会渲染。物品数量每 10 tick 刷新一次。

##### 剩余物品显示Overlay-X偏移

调整剩余物品 Overlay 的水平位置。

- 类型：`整数`
- 默认值：`0`
- 单位：像素
- 配置键：`remainItemOverlay-xOffset`
- 分类：`ALL`，`FUNCTIONS`

> 可以为负数，用于把 Overlay 移到屏幕左侧之外或调整对齐。

##### 剩余物品显示Overlay-Y偏移

调整剩余物品 Overlay 的垂直位置。

- 类型：`整数`
- 默认值：`0`
- 单位：像素
- 配置键：`remainItemOverlay-yOffset`
- 分类：`ALL`，`FUNCTIONS`

##### 苦力怕预警器

生存模式下检测玩家附近的苦力怕，发现后在动作栏提示距离并播放 TNT 引信音效。

- 类型：`布尔值 + 快捷键`
- 默认值：`true`
- 参考选项：`true`，`false`
- 配置键：`creeperWarner`
- 分类：`ALL`，`FUNCTIONS`

> 每 20 tick（约 1 秒）检测一次；只在生存模式下生效。

##### 苦力怕预警器范围

设置苦力怕预警器的检测半径，以玩家为中心。

- 类型：`浮点`
- 默认值：`8.0`
- 取值范围：`0` – `64`
- 单位：方块
- 配置键：`creeperWarner-Range`
- 分类：`ALL`，`FUNCTIONS`

##### 低生命值自动执行命令/发送聊天消息

生存模式下，当生命值低于阈值时自动发送聊天消息或执行命令。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`lowHealthAutoSendMessageOrCmd`
- 分类：`ALL`，`FUNCTIONS`

> 为避免刷屏，两次触发之间至少有约 50 秒的冷却。生命值必须**严格低于**阈值才会触发。

##### 生命值阈值

设置触发低生命值操作的阈值。

- 类型：`浮点`
- 默认值：`4.0`
- 取值范围：`1` – `20`
- 配置键：`healthValve`
- 分类：`ALL`，`FUNCTIONS`

##### 发送模式

设置低生命值时的触发方式。

- 类型：`选项`
- 默认值：`自动`
- 可选值：
  - `发送聊天消息`：始终发送下方「发送内容-消息」。
  - `发送命令`：始终执行下方「发送内容-指令」。
  - `自动`：命令可用时执行命令，否则回退为发送聊天消息。
- 配置键：`SendContent-Mode`
- 分类：`ALL`，`FUNCTIONS`

##### 发送内容-消息

设置低生命值时发送的聊天内容。

- 类型：`字符串`
- 默认值：`!s`
- 配置键：`SendContent-Message`
- 分类：`ALL`，`FUNCTIONS`

##### 发送内容-指令

设置低生命值时执行的命令。

- 类型：`字符串`
- 默认值：`spectator`
- 配置键：`SendContent-Command`
- 分类：`ALL`，`FUNCTIONS`

> 填写时不需要输入 `/`；即使输入了，模组也会自动去掉开头的 `/`。

##### 随时可以断开连接

在服务器重配置界面保持「断开连接」按钮可用，避免卡在连接/重连流程中无法退出。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`canAlwaysDisconnect`
- 分类：`ALL`，`FUNCTIONS`

##### 可在游戏中打开多人游戏菜单

在游戏内暂停菜单中添加「多人游戏屏幕」按钮，无需退出世界即可打开多人游戏列表。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`canOpenMutilPlayerScreenOnGaming`
- 分类：`ALL`，`FUNCTIONS`

#### TAB 菜单

##### TAB菜单过滤器

过滤 TAB 玩家列表，隐藏无用玩家或常驻假人。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`tabFilter`
- 分类：`ALL`，`FUNCTIONS`

过滤优先级如下，同一时间只会生效一个分支：

1. 启用「白名单」时，只显示白名单中的玩家。
2. 否则，启用「前缀」时，隐藏名称以任一前缀开头的玩家。
3. 否则，隐藏黑名单中的玩家。

相关列表位于「列表」分类：

- `tabFilterWhiteList`：白名单
- `tabFilterBlockList`：黑名单
- `tabFilterPrefix`：前缀

##### 启用TAB菜单过滤器白名单

启用后，TAB 列表只保留白名单中的玩家。

- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableTabFilterWhiteList`
- 分类：`ALL`，`FUNCTIONS`

##### 启用TAB菜单过滤器前缀

启用后，隐藏名称以指定前缀开头的玩家。

- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableTabFilter-Prefix`
- 分类：`ALL`，`FUNCTIONS`

#### 假人管理

##### 触发假人杀戮光环

按一次快捷键，对周围符合条件的假人执行一次 `player <名称> kill`。

- 类型：`快捷键`
- 默认值：`未绑定`
- 配置键：`triggerFakeKillAura`
- 分类：`ALL`，`FUNCTIONS`

> 只处理延迟为 0 的玩家（通常为假人）。这是**一次性触发**，不是持续光环；每次按下快捷键只执行一轮。

##### 假人杀戮光环范围

设置假人杀戮光环的检测半径，以玩家为中心。

- 类型：`浮点`
- 默认值：`4.0`
- 取值范围：`0` – `32`
- 单位：方块
- 配置键：`fakeKillAuraRange`
- 分类：`ALL`，`FUNCTIONS`

##### 启用假人杀戮光环黑名单

启用后，黑名单中的假人不会被杀戮光环处理，其余假人仍会被处理。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableFakeKillAuraBlackList`
- 分类：`ALL`，`FUNCTIONS`

##### 启用假人杀戮光环白名单

启用后，只有白名单中的假人会被杀戮光环处理。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableKillAuraWhiteList`
- 分类：`ALL`，`FUNCTIONS`

> 白名单优先于黑名单。两个列表都位于「列表」分类：`killAuraWhiteList`、`fakeKillAuraBlackList`。

##### 旁观假人TP菜单

在旁观者传送菜单中，把延迟为 0 的假人从普通玩家列表中分离出来，放入独立的「传送到假玩家」分类。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`spectatorTelePortMenu`
- 分类：`ALL`，`FUNCTIONS`

#### 挖掘与交互

##### 强制方块挖掘冷却

移植自 OMMC 的功能，强制客户端遵循原版的方块挖掘冷却。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`forceBlockMiningCoolDown`
- 分类：`ALL`，`FUNCTIONS`

> 每次继续破坏方块时会把客户端挖掘延迟设为 5 tick。如果安装了 Tweakeroo，还会同时关闭 Tweakeroo 的「禁用方块挖掘冷却」选项。

##### 平坦挖掘

移植自 OMMC 的功能。未潜行时，禁止破坏低于玩家脚部 Y 坐标的方块，避免误挖脚下或垂直下挖。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`flatMining`
- 分类：`ALL`，`FUNCTIONS`

> 潜行时可以临时解除该限制。

##### 右键告示牌执行命令

潜行并右键告示牌时，自动执行告示牌正面每一行以 `/` 开头的文本。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`useSignRunCommand`
- 分类：`ALL`，`FUNCTIONS`

> 只读取告示牌**正面**文本；每行必须以 `/` 开头。该操作会拦截这次方块交互，因此不会打开告示牌编辑界面。触发频率限制为约 1 次/秒。

##### 强制使用烟花火箭

右键时，如果主手或副手持有烟花火箭，优先直接使用烟花火箭，而不是先处理方块交互。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`forceUseFireWork`
- 分类：`ALL`，`FUNCTIONS`

> 适合鞘翅飞行时避免误点方块。只有烟花使用成功时才会取消本次交互。

#### 背包整理

##### 启用自动垃圾清理

打开容器界面时，自动识别并处理背包中的垃圾物品。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableInventoryCleaner`
- 分类：`ALL`，`FUNCTIONS`

> 需要安装 **ItemScroller**，且只在生存模式下生效。进入玩家背包或箱子等容器界面时触发。
>
> 潜影盒会递归检查内容物：只有盒内所有物品都符合清理条件时，整个潜影盒才会被处理。潜影盒本身不参与名单匹配，而是按盒内物品逐项判断。

##### 自动垃圾清理-切换模式

按快捷键在「丢出物品」与「转移至容器」之间切换清理模式，并在动作栏提示当前模式。

- 类型：`快捷键`
- 默认值：`未绑定`
- 配置键：`inventoryCleanerSwitchMode`
- 分类：`ALL`，`FUNCTIONS`

##### 自动垃圾清理-清理模式

设置垃圾物品的处理方式。

- 类型：`选项`
- 默认值：`丢出物品`
- 可选值：
  - `丢出物品`：在玩家背包界面把符合条件的物品丢出。
  - `转移至容器`：适合配合箱子使用；打开箱子界面时，符合条件的物品会被移入箱子。
- 配置键：`inventoryCleanerMode`
- 分类：`ALL`，`FUNCTIONS`

> 在箱子界面中，符合条件的物品始终会被移入箱子；清理模式主要影响玩家背包界面的处理方式。

##### 自动垃圾清理-保存背包物品

把当前背包中所有物品的 ID 快速保存到已启用的名单；潜影盒会展开为其内容物。

- 类型：`快捷键`
- 默认值：`未绑定`
- 配置键：`inventoryCleaner-SaveInventoryItem`
- 分类：`ALL`，`FUNCTIONS`

> 需要先启用白名单或黑名单。两者同时启用时，优先写入黑名单。

##### 启用自动垃圾清理白名单

启用后，**只有白名单中的物品**会被清理。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enbaleInventoryCleanerWhiteList`
- 分类：`ALL`，`FUNCTIONS`

##### 启用自动垃圾清理黑名单

启用后，**黑名单之外的物品**会被清理，名单中的物品会被保留。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enbaleinventoryCleanerBlackList`
- 分类：`ALL`，`FUNCTIONS`

> 黑名单优先于白名单。相关列表：`inventoryCleanerWhiteList`、`inventoryCleanerBlackList`（位于「列表」分类）。

##### 触发-潜影盒物品分离器

按下快捷键后保存主手物品，并自动打开背包主区域（不含快捷栏）中的所有潜影盒，将匹配该物品的内容物丢出。

- 类型：`快捷键`
- 默认值：`未绑定`
- 配置键：`trigger-boxItemSplitter`
- 分类：`ALL`，`FUNCTIONS`

> 需要安装 **ItemScroller** 与 **QuickShulker**。再次按下快捷键可提前停止。
>
> 只会扫描背包主区域（槽位 9–35）中的潜影盒，快捷栏中的盒子不会被打开。

#### 投影与方块

##### 启用自定义方块碰撞箱

把指定方块的**碰撞箱**替换为完整方块（1×1×1），用于穿过或踩在特殊形状的方块上。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableCustomBlockBlockShape`
- 分类：`ALL`，`FUNCTIONS`

> 只对本地玩家生效，不会改变服务端判定。白名单和黑名单建议只启用其中一个。
>
> 相关列表：`customBlockBlockShapeWhiteList`、`customBlockBlockShapeBlackList`。

##### 启用自定义方块碰撞箱白名单

启用后，只有白名单中的方块使用自定义碰撞箱。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableCustomBlockBlockShapeWhiteList`
- 分类：`ALL`，`FUNCTIONS`

##### 启用自定义方块碰撞箱黑名单

启用后，黑名单中的方块保持原版碰撞箱，其余方块使用自定义碰撞箱。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableCustomBlockBlockShapeBlackList`
- 分类：`ALL`，`FUNCTIONS`

##### 启用自定义方块交互碰撞箱

把指定方块的**交互/选中形状**替换为完整方块，影响方块轮廓与交互判定。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableCustomBlockBlockInteractionShape`
- 分类：`ALL`，`FUNCTIONS`

> 与「自定义方块碰撞箱」相互独立：前者影响移动碰撞，本项影响方块轮廓与交互。相关列表：`customBlockBlockInteractionShape-Whitelist`、`customBlockBlockInteractionShape-Blacklist`。

##### 启用自定义方块交互碰撞箱-白名单

启用后，只有白名单中的方块使用自定义交互形状。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableCustomBlockBlockInteractionShape-Whtelist`
- 分类：`ALL`，`FUNCTIONS`

##### 启用自定义方块交互碰撞箱-黑名单

启用后，黑名单中的方块保持原版交互形状，其余方块使用完整方块形状。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableCustomBlockBlockInteractionShape-Blacklist`
- 分类：`ALL`，`FUNCTIONS`

##### 启用自定义投影方块替换

在投影的 Easy Place 流程中，把原理图需要的方块替换为另一种方块进行放置。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableCustomLitematicaBlockReplace`
- 分类：`ALL`，`FUNCTIONS`

规则填写在「列表」分类的 `customLitematicaBlockReplaceList` 中，格式为：

```text
原始方块ID|替换方块ID
minecraft:oak_planks|minecraft:spruce_planks
```

表示当投影需要 `minecraft:oak_planks` 时，改用 `minecraft:spruce_planks` 作为所需物品并放置。

##### 启用自动盒子补货

当 Tweakeroo 的自动补货触发时，如果背包主区域（不含快捷栏）的潜影盒中有同类物品，自动打开潜影盒并把物品补到手上。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableAutoBlockRestrok`
- 分类：`ALL`，`FUNCTIONS`

> 需要安装 **Tweakeroo**（并开启自动补货）与 **QuickShulker**。黑名单优先于白名单。相关列表：`autoBlockRestrok-Whitelist`、`autoBlockRestrok-blackList`。

##### 启用自动盒子补货-白名单

启用后，只有白名单中的物品会触发自动盒子补货。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableAutoBlockRestrok-Whitelist`
- 分类：`ALL`，`FUNCTIONS`

##### 启用自动盒子补货-黑名单

启用后，黑名单中的物品不会触发自动盒子补货。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enableAutoBlockRestrok-blackList`
- 分类：`ALL`，`FUNCTIONS`

#### MiniHUD 集成

##### 挂载Logger信息到MiniHUD

把 TAB 玩家列表中显示的 Logger 信息追加到 MiniHUD 的 HUD 行中。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`mountLoggerOnMinihud`
- 分类：`ALL`，`FUNCTIONS`

> 需要安装 **MiniHUD**，并启用 MiniHUD 的 HUD。白名单/黑名单按「包含关键字」匹配。

##### 启用挂载Logger信息到MiniHUD-白名单

启用后，只显示名称中包含白名单关键字的 Logger 行。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enable-mountLoggerOnMinihudWhitelist`
- 分类：`ALL`，`FUNCTIONS`

##### 启用挂载Logger信息到MiniHUD-黑名单

启用后，隐藏名称中包含黑名单关键字的 Logger 行。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`enable-mountLoggerOnMinihudBlacklist`
- 分类：`ALL`，`FUNCTIONS`

> 白名单优先于黑名单。相关列表：`mountLoggerOnMinihudWhitelist`、`mountLoggerOnMinihudBlacklist`。

<a id="disables"></a>
### 二、禁用（DISABLES）

共 15 项，用于关闭原版或模组中不需要的行为。

#### 界面与渲染

##### 打开材料列表时禁用字幕

当投影的材料列表 HUD 被切换显示时，同步隐藏字幕，避免字幕与材料列表重叠。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableSubtitle`
- 分类：`ALL`，`DISABLES`

> 需要在 Litematica 中使用材料列表 HUD；字幕的隐藏状态会跟随材料列表 HUD 的显示/隐藏一起切换。

##### 禁用玩家盔甲渲染

隐藏玩家身上的盔甲图层，只影响玩家，其他实体不受影响。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disablePlayerArmorRender`
- 分类：`ALL`，`DISABLES`

> 在 1.21.10+ 中会保留鞘翅的渲染，其他盔甲部件会被隐藏。

##### 禁用TAB菜单背景

隐藏 TAB 玩家列表的背景，只保留玩家列表内容。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableTabBackGround`
- 分类：`ALL`，`DISABLES`

##### 禁用容器背景渲染

打开容器界面时不渲染背景，提高界面透明度。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableContainerBackGround`
- 分类：`ALL`，`DISABLES`

##### 禁用计分板背景

隐藏侧边栏计分板的背景。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableScoreBoardBackGround`
- 分类：`ALL`，`DISABLES`

##### 禁用字幕背景

隐藏字幕的黑色背景，仅显示字幕文本。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableSubtitleBackGround`
- 分类：`ALL`，`DISABLES`

##### 禁用掉落物旋转

禁用掉落物实体的旋转动画，使掉落物始终保持同一朝向。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disbaleItemEntityMulpose`
- 分类：`ALL`，`DISABLES`

#### 网络与加载

##### 禁用连接超时

拦截 `TimeoutException`，避免在加载超大投影或长时间卡顿时因连接超时被踢出。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableConnectionTimedOut`
- 分类：`ALL`，`DISABLES`

> 只取消超时异常的处理；如果服务器已经断开连接，本项无法恢复连接。

##### 禁用加载地形屏幕

隐藏进入世界时的「加载地形中」界面，理论上可以略微加快加入世界/服务器的速度。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableLevelLoadingScreen`
- 分类：`ALL`，`DISABLES`

##### 禁用数据包踢出

当收到无法解析或未知的数据包时，跳过该数据包而不是断开连接。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disablePacketKick`
- 分类：`ALL`，`DISABLES`

> 建议搭配 **ViaFabricPlus** 使用。跳过数据包可能导致部分内容不同步。

##### 禁用数据包踢出-强制进入服务器

忽略客户端不存在或未注册的数据包/注册表同步，强制进入服务器。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disablePacketKick-ForceJoinServer`
- 分类：`ALL`，`DISABLES`

> 适用于跨版本或服务端注册表不一致的情况。可能导致方块、物品或实体显示异常，请谨慎使用。

##### 禁用网络协议错误

阻止因网络协议错误（`onPacketError`）而被踢出服务器。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableNetworkProError`
- 分类：`ALL`，`DISABLES`

#### 其他

##### 禁用Profiler

禁用客户端 Profiler，减少性能分析带来的开销。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableProfiler`
- 分类：`ALL`，`DISABLES`

> 可以提升少量帧数，但 F3 的调试饼图将无法使用。

##### 禁用确认执行屏幕(1.21.10+)

跳过原版的命令确认执行界面，自动确认。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableEnsureScreen`
- 分类：`ALL`，`DISABLES`
- 版本：`1.21.10+`

##### 禁用客户端光照更新(26.1+)

跳过客户端的光照更新逻辑，在大量方块变化时显著提升帧数。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`disableClientLightUpdate`
- 分类：`ALL`，`DISABLES`
- 版本：`26.1+`

> 可能导致光照显示不更新或出现明暗异常，遇到问题时请关闭。

<a id="optimizations"></a>
### 三、优化（OPTIMIZATIONS）

共 10 项。优化类功能通常只影响客户端渲染/加载，不会改变服务端行为。

##### 展示框优化

优化展示框与地图画的渲染与数据存储，在大量展示框场景下可大幅提升平均帧与 1% Low 帧。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`optItemFrame`
- 分类：`ALL`，`OPTIMIZATIONS`
- 版本：`1.21.8+`（完整性能收益需要 `1.21.11+`）

优化内容包括：

- 跳过无意义或重复的展示框方块模型更新；
- 使用 `Int2ObjectOpenHashMap` 存储地图数据，降低 Map 查询与装箱开销；
- 跳过旋转为 0 的展示框的旋转矩阵运算；
- 对展示框使用固定光照值，减少光照计算。

> ⚠️ **注意**
>
> 由于 Mojang 在不同版本间的渲染代码改动较大，本功能仅在 **1.21.11+** 能保证完整性能收益。
>
> 测试数据（Minecraft 26.2 调优包，572 张地图画）：
>
> - 关闭优化：平均帧约 `370 FPS`，1% Low 约 `80 FPS`
> - 开启优化：平均帧约 `900 FPS`，1% Low 约 `430 FPS`
>
> 如果地图画不更新或出现渲染异常，请关闭本项或调整下方的可见性选项。

##### 展示框优化-展示框边框可见性(26.1+)

控制展示框方块模型（边框）的可见性，以在性能与外观之间取舍。

- 类型：`选项`
- 默认值：`无物品时可见(平衡)`
- 可选值：
  - `永远可见(低性能)`：始终渲染边框，最接近原版。
  - `无物品时可见(平衡)`：只有空展示框渲染边框，有物品时跳过。
  - `永不可见(高性能)`：始终不渲染边框，只显示物品。
- 配置键：`optItemFrame-FrameVisibility`
- 分类：`ALL`，`OPTIMIZATIONS`
- 版本：`26.1+`

##### 告示牌文本优化

通过缓存 `hasMessage()` 的结果提升告示牌渲染性能。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`optSignText`
- 分类：`ALL`，`OPTIMIZATIONS`

##### 朝向检测优化

优化 `Direction#byName` 的查找逻辑，减少方块朝向解析时的字符串比较开销。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`optDirection`
- 分类：`ALL`，`OPTIMIZATIONS`

##### MiniHUD掉帧优化

把 MiniHUD 的 HUD 行更新移到任务线程并复用缓存列表，降低 HUD 刷新造成的掉帧。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`optMiniHud`
- 分类：`ALL`，`OPTIMIZATIONS`

> 需要安装 **MiniHUD**。HUD 更新可能略有延迟。

##### 计分板优化

缓存计分板的分数列表，降低每帧重复查询与排序带来的开销。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`optScoreBoard`
- 分类：`ALL`，`OPTIMIZATIONS`

##### 实体光照优化

跳过实体的光照采样，直接使用固定光照值渲染实体，减少光照计算。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`optEntityLight`
- 分类：`ALL`，`OPTIMIZATIONS`

> 需要开启 **Tweakeroo** 的伽马覆写才能获得正常的视觉效果；否则实体可能显得过亮或过暗。

##### 快速模型渲染(26.2+)

合并连续且渲染类型相同的几何体，提升物品模型等内容的渲染效率。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`fastModelRender`
- 分类：`ALL`，`OPTIMIZATIONS`
- 版本：`26.2+`

##### 多线程投影加载

使用多线程加载投影，减少大型投影加载时的卡顿与等待时间。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`asyncLitematicaLoad`
- 分类：`ALL`，`OPTIMIZATIONS`

> 加载期间聊天栏会提示线程数与状态。若投影加载失败，请先关闭本项再重试。

##### 多线程投影加载线程数

设置多线程投影加载使用的线程数。

- 类型：`整数`
- 默认值：`2`
- 取值范围：`1` – 当前电脑的逻辑处理器数量
- 配置键：`asyncLitematicaLoadThreadAmount`
- 分类：`ALL`，`OPTIMIZATIONS`

> 线程数越大加载越快，但会占用更多 CPU。建议不超过逻辑核心数，并留出足够核心给游戏主线程。

<a id="commands"></a>
### 四、命令（COMMANDS）

共 4 项。命令相关功能需要服务端允许对应的 `/player` 命令。

##### /pm 命令

启用 `/pm` 命令，用于批量操作假人。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`commandPm`
- 分类：`ALL`，`COMMAND`

支持的格式：

```text
/pm <前缀> <开始值> <结束值> <Action>
/pm <开始值> <结束值> <Action>
```

- 第一种格式使用指定前缀；第二种格式使用「/pm 命令默认前缀」。
- 开始值最小为 `1`，结束值范围为 `1`–`1000`。
- 对每个编号 `i` 执行 `player <前缀><i> <Action>`，并在动作栏显示进度条。

常用 Action 示例：

```text
spawn
kill
use
dropStack all
attack interval 10
use perTick 64
attack
move forward
```

> 例如 `/pm bot_ 1 10 kill` 会依次让 `bot_1` 到 `bot_10` 执行 `kill`。需要服务端安装 Carpet 或等价的支持 `/player` 的模组，并拥有相应权限。

##### /pm 命令执行冷却

设置 `/pm` 连续执行 Action 时，每个假人之间的等待时间。

- 类型：`整数`
- 默认值：`10`
- 取值范围：`1` – `1000`
- 单位：毫秒
- 配置键：`commandPmExecutionCooldown`
- 分类：`ALL`，`COMMAND`

##### /pm 命令默认前缀

设置 `/pm` 命令在省略前缀时使用的默认假人名称前缀。

- 类型：`字符串`
- 默认值：`bot_`
- 配置键：`commandPmPrefix`
- 分类：`ALL`，`COMMAND`

##### 自动宝库命令 /autovault

提供 `/autovault` 命令，用于自动生成假人、定位宝库并循环开启宝库。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`commandAutoVault`
- 分类：`ALL`，`COMMAND`

命令格式：

```text
/autovault set <方块X> <方块Y> <方块Z>
/autovault player <前缀> <开始值> <结束值> <方块X> <方块Y> <方块Z>
/autovault start
/autovault stop
```

- `set`：把指定坐标的方块标记为宝库，坐标必须是宝库方块。
- `player`：设置假人名称前缀、编号范围以及假人出生坐标；朝向和俯仰角取执行命令时玩家的视角。
- `start` / `stop`：开始或停止自动循环。
- 运行时会循环执行：生成假人 → 等待假人在线 → 执行 `use` → 宝库可用后执行 `kill`。

> 需要服务端支持 `/player` 命令。切换世界时，运行状态与坐标会自动重置。

<a id="lists"></a>
### 五、列表（LISTS）

共 17 项，全部为字符串列表，默认均为空。列表本身不会生效，需要在「功能」或「LMS」分类中打开对应的启用开关。

#### 通用规则

- 每行一项，行内不要有多余空格。
- 物品/方块 ID 必须带命名空间，例如 `minecraft:diamond_pickaxe`。
- 玩家名按条目精确匹配；Logger 名单按「包含关键字」匹配。
- 白名单与黑名单同时启用时，不同功能的优先级不同，详见各条目。
- 修改后关闭配置界面即保存。

##### TAB菜单过滤器-白名单

- 类型：`字符串列表`
- 默认值：空
- 格式：玩家名，每行一个
- 配置键：`tabFilterWhiteList`
- 分类：`ALL`，`LISTS`
- 关联：启用「启用TAB菜单过滤器白名单」后，TAB 列表只显示这里的玩家。

##### TAB菜单过滤器-黑名单

- 类型：`字符串列表`
- 默认值：空
- 格式：玩家名，每行一个
- 配置键：`tabFilterBlockList`
- 分类：`ALL`，`LISTS`
- 关联：未启用白名单/前缀时，TAB 列表会隐藏这里的玩家。

##### TAB菜单过滤器-前缀

- 类型：`字符串列表`
- 默认值：空
- 格式：名称前缀，每行一个
- 配置键：`tabFilterPrefix`
- 分类：`ALL`，`LISTS`
- 关联：启用「启用TAB菜单过滤器前缀」后，隐藏名称以任一前缀开头的玩家。

##### 假人杀戮光环黑名单

- 类型：`字符串列表`
- 默认值：空
- 格式：玩家名，每行一个
- 配置键：`fakeKillAuraBlackList`
- 分类：`ALL`，`LISTS`
- 关联：启用「启用假人杀戮光环黑名单」后，名单中的假人不会被处理。

##### 假人杀戮光环白名单

- 类型：`字符串列表`
- 默认值：空
- 格式：玩家名，每行一个
- 配置键：`killAuraWhiteList`
- 分类：`ALL`，`LISTS`
- 关联：启用「启用假人杀戮光环白名单」后，只有名单中的假人会被处理；白名单优先于黑名单。

##### 自动垃圾清理-白名单

- 类型：`字符串列表`
- 默认值：空
- 格式：物品 ID，每行一个
- 配置键：`inventoryCleanerWhiteList`
- 分类：`ALL`，`LISTS`
- 关联：启用白名单后，只有名单中的物品会被清理。

##### 自动垃圾清理-黑名单

- 类型：`字符串列表`
- 默认值：空
- 格式：物品 ID，每行一个
- 配置键：`inventoryCleanerBlackList`
- 分类：`ALL`，`LISTS`
- 关联：启用黑名单后，名单外的物品会被清理，名单内的物品会被保留；黑名单优先于白名单。

##### 挂载Logger信息到MiniHUD-白名单

- 类型：`字符串列表`
- 默认值：空
- 格式：关键字，每行一个
- 配置键：`mountLoggerOnMinihudWhitelist`
- 分类：`ALL`，`LISTS`
- 关联：只显示名称中包含任一关键字的 Logger 行。

##### 挂载Logger信息到MiniHUD-黑名单

- 类型：`字符串列表`
- 默认值：空
- 格式：关键字，每行一个
- 配置键：`mountLoggerOnMinihudBlacklist`
- 分类：`ALL`，`LISTS`
- 关联：隐藏名称中包含任一关键字的 Logger 行；白名单优先于黑名单。

##### 自定义方块碰撞箱-白名单

- 类型：`字符串列表`
- 默认值：空
- 格式：方块 ID，每行一个
- 配置键：`customBlockBlockShapeWhiteList`
- 分类：`ALL`，`LISTS`
- 关联：启用碰撞箱白名单后，只有名单中的方块使用完整方块碰撞箱。

##### 自定义方块碰撞箱-黑名单

- 类型：`字符串列表`
- 默认值：空
- 格式：方块 ID，每行一个
- 配置键：`customBlockBlockShapeBlackList`
- 分类：`ALL`，`LISTS`
- 关联：启用碰撞箱黑名单后，名单中的方块保持原版碰撞箱。

##### 自定义方块交互碰撞箱-白名单

- 类型：`字符串列表`
- 默认值：空
- 格式：方块 ID，每行一个
- 配置键：`customBlockBlockInteractionShape-Whitelist`
- 分类：`ALL`，`LISTS`
- 关联：启用交互碰撞箱白名单后，只有名单中的方块使用完整方块形状。

##### 自定义方块交互碰撞箱-黑名单

- 类型：`字符串列表`
- 默认值：空
- 格式：方块 ID，每行一个
- 配置键：`customBlockBlockInteractionShape-Blacklist`
- 分类：`ALL`，`LISTS`
- 关联：启用交互碰撞箱黑名单后，名单中的方块保持原版形状。

##### 自定义投影方块替换列表

- 类型：`字符串列表`
- 默认值：空
- 格式：`原始方块ID|替换方块ID`，每行一条
- 配置键：`customLitematicaBlockReplaceList`
- 分类：`ALL`，`LISTS`
- 关联：配合「启用自定义投影方块替换」使用。

示例：

```text
minecraft:oak_planks|minecraft:spruce_planks
minecraft:stone|minecraft:cobblestone
```

##### 自动盒子补货-白名单

- 类型：`字符串列表`
- 默认值：空
- 格式：物品 ID，每行一个
- 配置键：`autoBlockRestrok-Whitelist`
- 分类：`ALL`，`LISTS`
- 关联：启用白名单后，只有名单中的物品会触发自动补货。

##### 自动盒子补货-黑名单

- 类型：`字符串列表`
- 默认值：空
- 格式：物品 ID，每行一个
- 配置键：`autoBlockRestrok-blackList`
- 分类：`ALL`，`LISTS`
- 关联：启用黑名单后，名单中的物品不会触发自动补货；黑名单优先于白名单。

##### 假人背包物品缓存-假人白名单

- 类型：`字符串列表`
- 默认值：空
- 格式：假人名称或名称前缀，每行一个
- 配置键：`fakeInvCache-Whitelist`
- 分类：`ALL`，`LISTS`
- 关联：只有标题以这些名称开头的假人背包会被缓存。

<a id="lms"></a>
### 六、LMS 假人取货（LMS）

共 12 项。该分类的功能需要服务端安装 **LMS Carpet Addition**，并提供 `getItem`、`getStorageData` 等命令。

#### 取货流程

1. 客户端通过 `getStorageData` 查询容器/假人库存，或直接使用 `getItem` 请求物品。
2. 服务端返回数据后，客户端解析出需要生成的假人与物品数量。
3. 自动生成假人，等待假人在线。
4. 可选：自动打开假人背包，把物品取出。
5. 可选：关闭背包后自动让假人下线。

#### 前置与限制

- 服务端必须支持对应的 LMS 命令，且玩家拥有执行权限。
- 如果 `getStorageData` 返回的 NBT 被折叠，需要安装 **Antideath-carpet-addition** 并开启 `fixNbtFold`，或使用 LMS 1.14.1+。
- 取货数据依赖聊天栏返回内容，请勿在取货过程中屏蔽相关消息。

##### 打开假人取货菜单

打开一个类似创造模式物品栏的界面，用于浏览已缓存的假人/容器库存并触发取货。

- 类型：`快捷键`
- 默认值：`未绑定`
- 配置键：`openFakeFetchGui`
- 分类：`ALL`，`LMS`

> 打开期间会锁定该界面，避免误操作。

##### 假人远程取货支持

LMS 假人取货功能的主开关。关闭后，客户端不会解析 LMS 返回的取货数据，也不会自动查询库存，取货菜单中的提交请求会被忽略。

- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`fakeFetchSupport`
- 分类：`ALL`，`LMS`

##### 假人取货自动查询

开启后，每隔约 20000 tick（约 16.7 分钟）自动执行一次 `getStorageData` 刷新库存数据。

- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`fakeAutoRefresh`
- 分类：`ALL`，`LMS`

> 需要同时开启「假人远程取货支持」，并且当前玩家可以使用 `getStorageData` 命令。

##### 中键投影取货

在投影中中键点击方块时，如果该方块在客户端世界中为空气（尚未放置）且背包内没有该物品，则自动向假人请求物品。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`middleClickLitematicBlockFetch`
- 分类：`ALL`，`LMS`

- 普通中键：请求 1 组（`物品最大堆叠数`）。
- `Shift + 中键`：请求 1 盒（`物品最大堆叠数 × 27`）。
- 只在生存模式下生效。

> 需要服务端安装 **LMS Carpet Addition**。若已缓存假人背包且其中包含该物品，会优先从缓存中对应的假人取货。

##### 假人取货自动打开假人背包

假人生成后，自动打开该假人的背包，方便直接取出物品。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`autoOpenFakeInv`
- 分类：`ALL`，`LMS`

##### 自动打开假人背包交互模式

设置自动打开假人背包时使用的交互方式。

- 类型：`选项`
- 默认值：`右键交互`
- 可选值：
  - `指令交互`：执行 `player <名称> inventory`。
  - `右键交互`：对假人实体使用主手右键。
- 配置键：`autoOpenFakeInv-Mode`
- 分类：`ALL`，`LMS`

##### 假人远程取货自动下线假人

关闭假人背包后，自动让该假人下线。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`autoKillFake`
- 分类：`ALL`，`LMS`

> 只有当前容器标题包含该假人名称时才会执行下线，避免误杀其他假人。

##### 假人远程取货自动打开背包/自动下线延迟

设置自动打开假人背包与自动下线之间（以及取货动作之间）的等待时间。

- 类型：`整数`
- 默认值：`100`
- 取值范围：`100` – `1000`
- 单位：毫秒
- 配置键：`autoActionCooldown`
- 分类：`ALL`，`LMS`

> 服务端响应较慢时适当调大，避免假人尚未生成就执行下一步。

##### 假人背包物品缓存

缓存假人背包中的物品。中键投影取货时会优先从缓存中寻找对应物品，材料列表也可以统计缓存内容。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`fakeInvCache`
- 分类：`ALL`，`LMS`

> 只有关闭标题以白名单名称开头的假人背包时才会缓存，且只在生存模式下缓存。材料列表统计还需要开启「投影材料列表-统计假人背包」。
>
> 相关列表：`fakeInvCache-Whitelist`（位于「列表」分类）。

##### 假人背包物品缓存-清理缓存

清除所有已缓存的假人背包数据，以及基于缓存统计出的假人库存数量。

- 类型：`快捷键`
- 默认值：`未绑定`
- 配置键：`fakeInvCache-clear`
- 分类：`ALL`，`LMS`

##### 投影材料列表-统计全物品

在 Litematica 材料列表中，把服务端 `getStorageData` 返回的库存数量也计入「可用数量」。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`MaterialList-CaculateLMS`
- 分类：`ALL`，`LMS`

##### 投影材料列表-统计假人背包

在 Litematica 材料列表中，把已缓存的假人背包内容也计入「可用数量」。

- 类型：`布尔值 + 快捷键`
- 默认值：`false`
- 参考选项：`true`，`false`
- 配置键：`MaterialList-CaculateInv`
- 分类：`ALL`，`LMS`

> 需要同时开启「假人背包物品缓存」并至少缓存一个假人背包。

<a id="config-file"></a>
## 配置文件与键名

配置文件路径：`.minecraft/config/asa.json`。关闭配置界面时自动保存。

### 键名

每个配置的键名即本文档中标注的「配置键」，位于 JSON 根对象的 `asa` 下。

- 普通布尔值、整数、浮点、字符串、字符串列表直接以值的形式保存。
- 「布尔值 + 快捷键」保存为对象：`enabled` 与 `hotkey`。
- 纯快捷键保存为对象：`keys`。
- 选项保存为当前选项的显示名称。

示例（仅展示结构，实际内容以游戏保存为准）：

```json
{
  "asa": {
    "creeperWarner": {
      "enabled": true,
      "hotkey": {
        "keys": ""
      }
    },
    "creeperWarner-Range": 8.0,
    "tabFilterWhiteList": [
      "Notch",
      "Herobrine"
    ],
    "SendContent-Mode": "自动",
    "openGui": {
      "keys": "Z,K"
    }
  }
}
```

> 建议在游戏关闭时手动编辑配置文件。游戏运行时，关闭配置界面会用内存中的值覆盖文件。

### 调试配置

以下 2 项只出现在「全部」页签，普通玩家无需修改：

| 名称 | 类型 | 默认值 | 配置键 | 说明 |
| --- | --- | --- | --- | --- |
| `debug` | 布尔值 + 快捷键 | `false` | `debug` | 在聊天栏和日志中输出调试信息 |
| `触发调试` | 快捷键 | `未绑定` | `触发调试` | 开发调试入口，当前为空实现 |

### 备份与重置

- 备份：复制 `config/asa.json` 到其他位置。
- 重置单项：在配置界面点击对应选项的重置按钮（若可用）。
- 重置全部：关闭游戏后删除 `config/asa.json`，下次启动会重新生成默认配置。

<a id="compatibility"></a>
## 兼容性与注意事项

### 客户端模组

ASA 是纯客户端模组，服务器无需安装。但以下功能依赖服务端能力：

- 假人相关功能（杀戮光环、`/pm`、`/autovault`）需要服务端支持 `/player` 命令。
- LMS 取货相关功能需要服务端安装 **LMS Carpet Addition**，并拥有相应命令权限。
- 部分服务器会将自动取货、杀戮光环、强制进入服务器等行为视为违规，请先确认服务器规则。

### 依赖与功能对照

| 功能 | 需要的模组/环境 |
| --- | --- |
| 投影材料列表、中键取货、方块替换 | Litematica |
| 挂载 Logger、MiniHUD 掉帧优化 | MiniHUD |
| 自动盒子补货、强制方块挖掘冷却 | Tweakeroo（自动补货需要开启） |
| 自动垃圾清理、潜影盒物品分离器 | ItemScroller |
| 自动盒子补货、潜影盒物品分离器 | QuickShulker |
| 假人取货、中键取货、库存统计 | LMS Carpet Addition（服务端） |
| `/pm`、`/autovault`、杀戮光环 | Carpet 或支持 `/player` 的服务端 |

### 已知交互

- **强制方块挖掘冷却**开启时，会覆盖 Tweakeroo 的「禁用方块挖掘冷却」选项。
- **自定义方块碰撞箱**对 Lithium 的碰撞上下文做了兼容处理。
- **展示框优化**会跳过地图与展示框的部分更新逻辑；如果地图画不刷新，请关闭本项。
- **禁用客户端光照更新**可能导致光照异常，遇到问题请关闭。
- **禁用数据包踢出 / 强制进入服务器**可能导致方块、实体或注册表不同步，建议搭配 ViaFabricPlus 使用。
- **实体光照优化**需要 Tweakeroo 的伽马覆写才能获得正常视觉表现。

### 多版本注意事项

- 不同 Minecraft 版本的 jar 不能混用。
- 1.21.x 使用 Java 21；26.1 及以上使用 Java 25。
- 部分功能有版本门槛：
  - 禁用确认执行屏幕：`1.21.10+`
  - 展示框边框可见性、禁用客户端光照更新：`26.1+`
  - 快速模型渲染：`26.2+`
  - 展示框优化的完整性能收益：`1.21.11+`

<a id="faq"></a>
## 常见问题

### 按 `Z,K` 没有打开设置界面

- 检查快捷键是否被其他模组占用，或在 malilib 的快捷键设置中重新绑定。
- 也可以通过 Mod Menu 的模组列表进入 ASA 配置。
- 确认 malilib 已安装，且版本与游戏版本匹配。

### 修改配置后没有生效

- 关闭配置界面才会保存；部分渲染类选项需要重新打开界面或切换维度。
- 手动编辑 `asa.json` 时请先关闭游戏，否则会被游戏内配置覆盖。
- 检查对应功能的「启用」开关是否也已打开。

### 列表填了但没有效果

- 物品/方块 ID 必须带命名空间（如 `minecraft:stone`）。
- 玩家名按条目精确匹配（区分大小写）。
- 检查对应的「启用白名单/黑名单」开关是否打开。
- 注意各功能的优先级，例如杀戮光环白名单优先于黑名单。

### 中键投影取货没有反应

- 确认已开启「中键投影取货」，并且处于生存模式。
- 目标方块必须是投影中尚未放置的方块（客户端世界为空气），且背包内该物品数量为 0。
- 服务端需要安装 LMS Carpet Addition，并允许使用 `getItem`。
- 如果使用缓存取货，确认已开启「假人背包物品缓存」并缓存过对应假人。

### 自动盒子补货没有反应

- 需要安装 Tweakeroo 并开启自动补货，同时安装 QuickShulker。
- 潜影盒需要在背包主区域（不含快捷栏），且其中包含目标物品。
- 检查白名单/黑名单是否把目标物品排除。

### 潜影盒物品分离器没有反应

- 需要安装 ItemScroller 与 QuickShulker。
- 主手必须持有要分离的物品，背包主区域中要有对应的潜影盒。
- 快捷栏中的潜影盒不会被打开。

### 自动垃圾清理没有反应

- 需要安装 ItemScroller，且处于生存模式。
- 需要打开容器界面（玩家背包或箱子等）才会触发。
- 检查白名单/黑名单与清理模式设置。

### 展示框优化后地图画不更新或画面异常

- 尝试关闭「展示框优化」，或切换「展示框优化-展示框边框可见性」。
- 该功能仅在 1.21.11+ 能保证完整性能收益，旧版本可能出现兼容问题。

### 取货时提示 NBT 被折叠

安装 **Antideath-carpet-addition** 并开启 `fixNbtFold` 规则，或升级到 LMS 1.14.1+。

### 低生命值没有执行命令

- 确认生命值严格低于阈值。
- 检查命令是否有权限；「自动」模式在命令不可用时会回退为发送聊天消息。
- 触发后有约 50 秒冷却。

### 配置文件在哪里？如何恢复默认？

配置文件为 `.minecraft/config/asa.json`。关闭游戏后删除该文件即可恢复默认配置。

<a id="build"></a>
## 从源码构建

需要 JDK 21（1.21.x 分支）或 JDK 25（26.x 分支）以及 Git。

```bash
git clone https://github.com/jfglzs/Antideath-survival-assistant.git
cd Antideath-survival-assistant
./gradlew :26.2:build
```

> Windows 下使用 `gradlew.bat :26.2:build`。

构建产物位于 `versions/<版本>/build/libs/`。项目使用 [Stonecutter](https://stonecutter.kikugie.dev/) 管理多版本，构建其他分支时替换任务中的版本号即可。

<a id="credits"></a>
## 致谢与许可

### 致谢

- 感谢 [@OptiJava](https://github.com/OptiJava) 的指导。
- 感谢 [masa](https://masa.dy.fi/) 及其模组生态（malilib、Litematica、MiniHUD、Tweakeroo、ItemScroller）。
- 感谢 [Sakura-Ryoko](https://github.com/sakura-ryoko) 维护的 malilib 等模组分支。
- 感谢 LMS Carpet Addition、Antideath-carpet-addition 等项目的服务端支持。
- 感谢所有提交问题与建议的玩家。

### 许可

本项目使用 **CC0-1.0** 许可，详情见 [LICENSE](https://github.com/jfglzs/Antideath-survival-assistant/blob/master/LICENSE)。

### 反馈

- 问题反馈：[GitHub Issues](https://github.com/jfglzs/Antideath-survival-assistant/issues)
- 下载发布：[GitHub Releases](https://github.com/jfglzs/Antideath-survival-assistant/releases) / [Modrinth](https://modrinth.com/mod/Antideath-survival-assistant)
