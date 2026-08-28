package com.example.nullpointermod.item;

import com.example.nullpointermod.NullPointerMod;
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
    private static final int COOLDOWN_TICKS = 100; // 5秒
    private static final int MAX_NULL_POINTERS = 64;

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
            int count = countNullPointers(level);
            if (count >= MAX_NULL_POINTERS) {
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                        "§c已达到空指针上限（64个），无法生成更多！"
                ));
                player.getCooldowns().addCooldown(this, 20);
                return InteractionResultHolder.fail(heldStack);
            }

            ItemStack nullPointerStack = new ItemStack(NullPointerMod.NULL_POINTER_ITEM.get());
            if (!player.getInventory().add(nullPointerStack)) {
                player.drop(nullPointerStack, false);
            }

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "§a成功生成一个空指针！当前总量：" + (count + 1) + "/64"
            ));
        }

        return InteractionResultHolder.success(heldStack);
    }

    private int countNullPointers(Level level) {
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
