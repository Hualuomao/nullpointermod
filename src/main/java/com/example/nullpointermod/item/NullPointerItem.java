package com.example.nullpointermod.item;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import com.example.nullpointermod.entity.NullPointerProjectile;

/**
 * Null Pointer Item - Right-click to shoot projectile
 * Stack size: 1, consumes self on use
 */
public class NullPointerItem extends Item {

    public NullPointerItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Create and shoot projectile
            NullPointerProjectile projectile = new NullPointerProjectile(level, player);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectile);

            // Consume the item
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
        }

        return InteractionResultHolder.success(itemStack);
    }
}
