# NullPointerMod 🔥

> *“Java 程序员最怕的噩梦，现在可以在 Minecraft 里亲手扔出去了！”*

[![Forge](https://img.shields.io/badge/Forge-47.3.0-blue)](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)
[![Build Status](https://img.shields.io/github/actions/workflow/status/Hualuomao/nullpointermod/build.yml?branch=main)](https://github.com/Hualuomao/nullpointermod/actions)

---

## 📖 简介

**NullPointerMod** 是一个恶搞向模组，将 Java 编程中最著名的异常 —— `java.lang.NullPointerException` —— 变成了一件**可以投掷的终极武器**！

合成稀有物品“Java”，右键生成“空指针”抛射物，然后……**让目标客户端原地崩溃**！整蛊好友、制造节目效果，或者体验一把“自爆”的刺激感，全凭你喜欢。

> ⚠️ **注意**：这是一个 **恶搞模组**，请勿在严肃的服务器中滥用。客户端崩溃不会损坏存档，只会退出游戏回到启动器。

---

## ✨ 核心玩法

### 1️⃣ 合成“Java”物品

使用稀有材料合成“Java”物品：

| 材料 | 数量 |
|------|------|
| 末影之眼（Ender Eye） | 2 个 |
| 下界之星（Nether Star） | 1 个 |
| 下界合金锭（Netherite Ingot） | 2 个 |

**合成配方**（工作台）：
E
I N I
E
*E = 末影之眼 · N = 下界之星 · I = 下界合金锭*

---

### 2️⃣ 生成“空指针”物品

手持“Java”物品，**对着空气右键**（不需要目标方块），即可生成一个 **“Java 空指针异常”** 物品。

**生成限制**（防滥用）：
- ⚡ 每位玩家最多生成 **16 个**
- 🌍 全服务器最多同时存在 **128 个**
- ⏰ 掉在地上的空指针 **30 秒后自动消失**
- 🕐 每次生成有 **5 秒冷却时间**

---

### 3️⃣ 发射“空指针”抛射物

手持“空指针”物品右键，向前方射出一颗抛射物。

| 命中目标 | 效果 |
|----------|------|
| **方块** | 发射者的客户端 **立刻崩溃**（回到启动器） |
| **自己** | 发射者 **自爆崩溃**（恶趣味拉满） |
| **其他生物/玩家** | 无事发生（留给整蛊更多操作空间） |
| **飞行超过 10 秒** | 发射者的客户端 **崩溃**（超时） |

**关键机制**：所有崩溃由服务端向客户端发送指令触发，**服务端本身不会崩溃**，其他玩家完全不受影响！

---

## 🎯 设计理念

### ❓ 为什么这样设计？

- **Java 物品** = 可复用的“生成器”（稀有、无限使用但有冷却）
- **空指针物品** = 一次性“弹药”（用完即消失，有数量上限）
- **崩客户端不崩服** = 恶搞的同时保护服务器稳定
- **高成本合成** = 确保只有后期玩家才能使用，不破坏前期平衡

---

## 🛡️ 服务器安全

| 机制 | 说明 |
|------|------|
| **服务端不抛异常** | 所有 `NullPointerException` 只在客户端抛出 |
| **全局物品上限** | 最多 128 个空指针同时存在，防止内存溢出 |
| **个人生成上限** | 每人最多 16 个，防止单玩家刷爆 |
| **掉落物自动清理** | 30 秒后消失，地面不会堆积 |
| **右键冷却** | 5 秒冷却，防止连点刷屏 |

---

## 📷 截图

> *（你可以在这里放游戏内截图）*

| Java 物品 | 空指针物品 | 抛射物飞行 |
|-----------|------------|------------|
| `(待补充)` | `(待补充)` | `(待补充)` |

---

## 📦 依赖与兼容性

| 项目 | 版本 |
|------|------|
| **Minecraft** | 1.20.1 |
| **Forge** | 47.3.0 及以上 |
| **其他模组** | 无需任何前置模组 |

---

## 🔧 技术实现亮点

- 📦 使用 `DeferredRegister` 注册物品和实体
- 🌐 自定义 `SimpleChannel` 网络通信，服务端发送崩溃指令
- 🧠 玩家 `PersistentData` 存储个人生成计数
- 🎯 抛射物碰撞检测与自爆逻辑
- 📊 全局物品计数，防止滥用

---

## ⚙️ 命令与配置

> *（本模组无命令和配置文件，开箱即用）*

---

## 🚀 如何安装

1. 下载本模组的 `.jar` 文件（从 [Releases](https://github.com/Hualuomao/nullpointermod/releases) 获取）
2. 放入 Minecraft 客户端的 `mods` 文件夹
3. 启动游戏，确保 Forge 1.20.1 已安装
4. 进入世界，开始整蛊！

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！如果你有好的想法或发现了 Bug，请到 [GitHub Issues](https://github.com/Hualuomao/nullpointermod/issues) 反馈。

### 开发环境搭建

```bash
# 克隆仓库
git clone https://github.com/Hualuomao/nullpointermod.git
cd nullpointermod

# 构建模组
./gradlew build

# 构建产物在 build/libs/ 目录下
