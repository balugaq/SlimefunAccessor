# AGENTS.md

本文件给在本仓库工作的 AI 助手（Agent）看，用于快速建立项目心智模型。
人类贡献者请先读 `CONTRIBUTING.md` 与 `CODE_OF_CONDUCT.md`。

## 项目是什么

SlimefunAccessor 是 Minecraft（Spigot/Paper）插件，Slimefun4 的附属插件（addon），作者 balugaq。

核心功能只有一个机器：**Accessor（访问器）**。

- 放置后自动扫描附近的所有 Slimefun 机器（默认半径 100 格），在 54 格 GUI 中集中展示；
- 左键图标 = 远程打开该机器的菜单；右键 = 粒子 ESP 高亮定位方块；
- Shift+左键 = 给机器加自定义标签；Shift+右键 = 清除标签；
- 支持关键词搜索（装了 JustEnoughGuide 时还能用它的过滤器语法和拼音搜索）；
- 可自动识别机器旁边展示框（ItemFrame）里的物品并用作标签；
- 扫描半径可通过 GUI 设置（1~32，见「已知不一致」）。

## 技术栈

- Java 16（`maven.compiler.source/target = 16`，不要擅自升级）
- Maven，`defaultGoal = clean package`，产物 `target/SlimefunAccessor v${version}.jar`
- maven-shade-plugin 3.6.0（`minimizeJar=true`，打包时剔除 `META-INF/*`）
- spigot-api 1.21-R0.1-SNAPSHOT（provided）
- Slimefun4（jitpack，provided，硬依赖）
- GuizhanLibPlugin 1.7.6（provided，softdepend）
- JustEnoughGuide（`libs/JustEnoughGuide.jar`，system scope，softdepend）
- Lombok 1.18.34（provided，IDE 需要装 Lombok 插件）
- bstats-bukkit 3.0.2（compile，会打进 jar）
- findbugs annotations 3.0.1u2

## 构建与验收

- 构建：`mvn clean package`
- 没有单元测试框架，**编译通过 + 静态代码审查即为验收标准**。
- 不要自行启动 Minecraft 服务器或浏览器做运行期验证；运行期表现由主人在测试服目视确认。静态推理的结论必须标注「待目视确认」，不能当视觉验收上报。
- `target/`、`dependency-reduced-pom.xml` 是构建产物，不进 git。

## 项目结构

```
src/main/java/com/balugaq/slimefunaccessor/
├── implementation/             # 插件自身内容
│   ├── main/                   # 入口 SlimefunAccessorPlugin、物品/配方/图标/分组注册
│   ├── slimefun/               # Accessor 机器本体
│   └── listeners/              # BlockListener（机器增删时更新可达列表）、InventoryListener（远程菜单关闭后返回）
└── libraries/                  # 可复用框架代码
    ├── slimefun/               # CustomSlimefunItem → AccessorItem → MenuItem 继承链
    │   └── foreground/         # AccessorForeground：GUI 字符矩阵 + 全部按钮行为
    ├── managers/               # Manager/ConfigManager/ListenerManager/IntegrationManager
    └── utils/                  # Pager（分页）、ChatUtil（对话输入）、PdcUtil、ParticleUtil 等
```

## 核心概念（改代码前必读）

1. **Accessor 生命周期**：放置 → `Accessor.load()` 把半径内所有 Slimefun 机器位置灌进 `Pager<Location>`；`BlockTicker`（异步）周期性触发 UPDATE_MENU；破坏 → `unload()` 清空连接。新机器被放置/破坏时由 `BlockListener` → `AccessorForeground.addAccessible/removeAccessible` 增量更新。
2. **持久化状态**存在 Slimefun 的 BlockData 里（`StorageCacheUtils`），键名只有两个：`filter`（搜索关键词）、`range`（半径字符串，`Accessor.BS_FILTER_KEY` / `BS_RANGE_KEY`）。加新状态沿用这套，别自建存储。
3. **GUI 布局**用 `MenuMatrix` 字符矩阵定义（`AccessorForeground.MATRIX`）：`D`=展示格，`X/R/H/S/P/N`=自动标记/范围/帮助/搜索/上页/下页。每个按钮的行为是 `AccessorForeground.Behavior` 里的一个 `BehaviorHandler` 常量。改界面先改矩阵和图标，再在 `applyBlockMenu` 里绑 handler。
4. **重绘驱动**是 `Pager.setDirty(true)` + `Behavior.UPDATE_MENU`，没有事件总线，别绕开这套自己刷界面。
5. **物品继承链**：`CustomSlimefunItem` → `AccessorItem`（封装 `register`）→ `MenuItem`（封装 `BlockMenuPreset`）。新机器继承 `MenuItem`，在 `AccessorItems.setup()` 注册，配方放 `AccessorRecipes`，图标放 `AccessorIcons`，分组放 `AccessorItemGroups`。
6. **JEG 集成**：`IntegrationManager` 检测 JustEnoughGuide 是否加载；搜索命中判定会走 `SearchGroup` 的过滤器与拼音逻辑，解析逻辑在 `parseSearchTerm`。

## 代码约定

- 方法参数尽量 `final`；公开成员加 `@Nonnull`/`@Nullable` + JavaDoc（CONTRIBUTING.md 明确要求）。
- `implementation/` 放插件内容，`libraries/` 放可复用框架代码，新增文件按此归位。
- 面向玩家的提示消息目前是中文，保持一致。
-  Lombok `@Getter` 等注解可用，但注意与 shade 的兼容（`minimizeJar` 只裁依赖，不裁自己的类）。

## 已知问题 / 坑

- **范围不一致**：`Accessor.DEFAULT_RANGE = 100`，但 RANGE 按钮 UI 提示和输入校验都是 1~32。改范围逻辑前先和主人确认预期行为。
- `libs/JustEnoughGuide.jar` 是 system scope 本地 jar，缺失会导致 Maven 解析失败，不要删。
- `maven-shade-plugin` 的 `minimizeJar=true` 会裁掉未引用到的依赖类，引入依赖反射/SPI 的库时要验证产物。
- `plugin.yml` 里 `version: ${project.version}` 靠 Maven resource filtering 注入，不要手填版本号。
- 插件实例是单例 `SlimefunAccessorPlugin.instance()`，调度任务统一走它。
- **不要在本仓库执行 `git commit` / `push` / `rebase` / `reset` 等写操作**——提交权归主人，只把工作区留到可通过验收的状态，并给出建议的提交信息。

## 相关文档

- `CONTRIBUTING.md`：PR 流程（先 Discord 讨论、补 JavaDoc、按 SemVer 升版本号）
- `CODE_OF_CONDUCT.md`：行为准则
- 上游：https://github.com/balugaq/SlimefunAccessor
