package com.example.nullpointermod;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class NullPointerProjectile extends ThrowableProjectile {
    private boolean hitEntity = false;
    private boolean hitBlock = false;

    public NullPointerProjectile(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    public NullPointerProjectile(Level level, LivingEntity shooter) {
        super(NullPointerMod.NULL_POINTER_PROJECTILE.get(), shooter, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        
        if (!this.level().isClientSide) {
            Entity target = result.getEntity();
            
            // 不伤害射手
            if (target == this.getOwner()) {
                return;
            }
            
            if (target instanceof LivingEntity living) {
                // 秒杀：直接设置生命值为0
                living.setHealth(0.0F);
                hitEntity = true;
            }
            
            this.discard(); // 移除抛射物
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        
        if (!this.level().isClientSide) {
            // 如果不是击中实体的结果，说明击中了方块或其他
            if (!(result instanceof EntityHitResult)) {
                hitBlock = true;
                this.discard();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            // 如果未击中实体但已存在超过200 tick（10秒），或者击中了方块，则抛出异常
            if ((this.tickCount > 200 || hitBlock) && !hitEntity) {
                // 抛出异常，导致游戏崩溃
                throw new NullPointerException(
                    "A wild java.lang.NullPointerException appears! " +
                    (hitBlock ? "You hit a block instead of an entity!" : "The projectile timed out without hitting anything!")
                );
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        // 1.20.1中不需要特殊处理
    }
}
