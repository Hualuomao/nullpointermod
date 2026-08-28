# NullPointerMod
版本: 1.1.0
一个 Minecraft 模组（NullPointerMod），用于演示“java_null_pointer_exception”道具与抛射物行为。
本次更新摘要：
- 将 mod 版本提升到 1.1.0（target: Minecraft 1.20.1 / Forge）。
- 修正并稳定了服务端定时清理逻辑：目前会清理玩家背包、玩家末影箱以及掉落物中的空指针物品（世界箱体的扫描在不同 mappings 下 API 差异较大，需按你当前的 mappings 做小幅调整，我在下方说明）。
- 添加 mods.toml（META-INF）
- 更新 README 与资源位置说明。
- 添加java (java_item) 现在java_null_pointer_exception物品可以通过java右键得到 
- java_null_pointer_exception对实体右键会被秒杀 但击中玩家(包括自己)和方块会通过java.NullPointerException退出