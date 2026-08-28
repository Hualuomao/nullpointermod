package com.example.nullpointermod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.nbt.CompoundTag;

/**
 * Null Pointer Projectile Entity
 * Triggers crash on: self-hit, block-hit, or timeout
 */
public class NullPointerProjectile extends Projectile {

    private static final int MAX_LIFETIME = 20 * 20; // 20 seconds in ticks
    private int lifetime = 0;

    public NullPointerProjectile(Level level, Player player) {
        super(ModEntities.NULL_POINTER_PROJECTILE.get(), level);
        this.setOwner(player);
    }

    public NullPointerProjectile(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        lifetime++;

        // Check if lifetime exceeded
        if (lifetime >= MAX_LIFETIME) {
            triggerCrash("Timeout");
            this.discard();
            return;
        }

        // Check for entity collision (except owner)
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(this.level(), this, 
            entity -> entity != this.getOwner());
        
        if (entityHitResult != null) {
            triggerCrash("Entity hit: " + entityHitResult.getEntity().getType().getDescription().getString());
            this.discard();
            return;
        }

        // Check for block collision
        BlockHitResult blockHitResult = this.level().clip(new net.minecraft.world.phys.ClipContext(
            this.getEyePosition(), 
            this.position().add(this.getDeltaMovement()),
            net.minecraft.world.phys.ClipContext.Block.COLLIDER,
            net.minecraft.world.phys.ClipContext.Fluid.NONE,
            this));

        if (blockHitResult.getType() != net.minecraft.world.phys.HitResult.Type.MISS) {
            triggerCrash("Block hit at " + blockHitResult.getBlockPos());
            this.discard();
            return;
        }
    }

    private void triggerCrash(String reason) {
        // Send crash packet to all players
        // This will be implemented via network packet
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Lifetime", lifetime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.lifetime = compound.getInt("Lifetime");
    }
}
