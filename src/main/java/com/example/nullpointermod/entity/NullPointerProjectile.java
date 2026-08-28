package com.example.nullpointermod.entity;

import com.example.nullpointermod.NullPointerMod;
import com.example.nullpointermod.network.ClientboundCrashPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PacketDistributor;

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
        Entity target = result.getEntity();
        Entity owner = this.getOwner();

        if (target == owner) {
            if (!this.level().isClientSide()) {
                if (owner instanceof ServerPlayer player) {
                    NullPointerMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                            new ClientboundCrashPacket("You hit yourself with a NullPointer! Self-destruct!")
                    );
                }
                this.discard();
            }
            return;
        }

        this.hitEntity = true;
        super.onHitEntity(result);
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.BLOCK) {
            this.hitBlock = true;
        }
        super.onHit(result);
    }

    @Override
    public void tick() {
        super.tick();

        if ((this.tickCount > 200 || hitBlock) && !hitEntity) {
            Level level = this.level();

            if (level.isClientSide()) {
                throw new NullPointerException("A wild NullPointerException appears! (Client tick)");
            }

            Entity owner = this.getOwner();
            if (owner instanceof ServerPlayer player) {
                NullPointerMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                        new ClientboundCrashPacket("You hit a block instead of an entity!")
                );
            }
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {
        // 无数据需要同步
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
