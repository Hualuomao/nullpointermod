package com.example.nullpointermod;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NullPointerItem extends Item {
    public NullPointerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        // 客户端只播放声音
        if (level.isClientSide) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F);
            return InteractionResultHolder.success(stack);
        }
        
        // 服务端生成实体
        NullPointerProjectile projectile = new NullPointerProjectile(level, player);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
        level.addFreshEntity(projectile);
        
        // 设置冷却（1秒 = 20 tick）
        player.getCooldowns().addCooldown(this, 20);
        
        return InteractionResultHolder.success(stack);
    }
}
