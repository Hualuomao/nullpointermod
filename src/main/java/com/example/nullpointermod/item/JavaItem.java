package com.example.nullpointermod.item;

import com.example.nullpointermod.NullPointerMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class JavaItem extends Item {
    private static final int COOLDOWN_TICKS = 100;          // 5 秒
    private static final int MAX_PLAYER_GENERATIONS = 16;   // 每个玩家最多生成 16 个
    private static final int MAX_GLOBAL_COUNT = 128;        // 全局最多 128 个
    private static final int ITEM_LIFETIME_SECONDS = 30;    // 掉落物 30 秒后消失

    public JavaItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(heldStack);
        }

        if (!level.isClientSide()) {
            // 1. 检查全局数量
            int globalCount = countAllNullPointers(level);
            if (globalCount >= MAX_GLOBAL_COUNT) {
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                        "§c服务器空指针已达上限（" + MAX_GLOBAL_COUNT + " 个），无法生成！"
                ));
                player.getCooldowns().addCooldown(this, 20);
                return InteractionResultHolder.fail(heldStack);
            }

            // 2. 检查玩家已生成次数
            CompoundTag persistentData = player.getPersistentData();
            int playerGenerated = persistentData.getInt("nullpointer_generated");
            if (playerGenerated >= MAX_PLAYER_GENERATIONS) {
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                        "§c你已达到个人生成上限（" + MAX_PLAYER_GENERATIONS + " 个）！"
                ));
                player.getCooldowns().addCooldown(this, 20);
                return InteractionResultHolder.fail(heldStack);
            }

            // 3. 生成空指针物品
            ItemStack nullPointerStack = new ItemStack(NullPointerMod.NULL_POINTER_ITEM.get());
            if (!player.getInventory().add(nullPointerStack)) {
                ItemEntity itemEntity = player.drop(nullPointerStack, false);
                if (itemEntity != null) {
                    itemEntity.lifespan = ITEM_LIFETIME_SECONDS * 20; // 20 ticks/s
                }
            }

            // 4. 更新玩家计数
            persistentData.putInt("nullpointer_generated", playerGenerated + 1);

            // 5. 冷却 + 提示
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "§a生成空指针！当前服务器总量：" + (globalCount + 1) + "/" + MAX_GLOBAL_COUNT
            ));
        }

        return InteractionResultHolder.success(heldStack);
    }

    private int countAllNullPointers(Level level) {
        int total = 0;

        for (Player player : level.players()) {
            for (ItemStack stack : player.getInventory().items) {
                if (stack.getItem() == NullPointerMod.NULL_POINTER_ITEM.get()) {
                    total += stack.getCount();
                }
            }
            if (player.getEnderChestInventory() != null) {
                var enderChest = player.getEnderChestInventory();
                for (int i = 0; i < enderChest.getContainerSize(); i++) {
                    ItemStack stack = enderChest.getItem(i);
                    if (stack.getItem() == NullPointerMod.NULL_POINTER_ITEM.get()) {
                        total += stack.getCount();
                    }
                }
            }
        }

        AABB worldAABB = new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, worldAABB,
                e -> e.getItem().getItem() == NullPointerMod.NULL_POINTER_ITEM.get());
        for (ItemEntity itemEntity : items) {
            total += itemEntity.getItem().getCount();
        }

        return total;
    }
}
