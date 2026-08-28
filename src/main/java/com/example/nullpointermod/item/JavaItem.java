package com.example.nullpointermod.item;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

/**
 * Java Item - Right-click on air to generate a null pointer
 * Does not consume, has 5-second cooldown (global cooldown 64 ticks)
 */
public class JavaItem extends Item {

    private static final int COOLDOWN_TICKS = 100; // 5 seconds at 20 ticks/second

    public JavaItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide && player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(itemStack);
        }

        if (!level.isClientSide) {
            // Generate a null pointer (NullPointerItem) in player's inventory
            ItemStack nullPointer = new ItemStack(ModItems.NULL_POINTER_ITEM.get(), 1);
            if (!player.addItem(nullPointer)) {
                player.drop(nullPointer, false);
            }

            // Set cooldown
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return InteractionResultHolder.success(itemStack);
    }
}
