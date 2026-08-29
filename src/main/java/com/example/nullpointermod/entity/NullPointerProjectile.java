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
import net.minecraft.world.entity.player.Player;
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
        NullPointerMod.LOGGER.info("空指针抛射物已创建，ID: {}", this.getId());
    }

    public NullPointerProjectile(Level level, LivingEntity shooter) {
        super(NullPointerMod.NULL_POINTER_PROJECTILE.get(), shooter, level);
        NullPointerMod.LOGGER.info("空指针抛射物已创建，发射者: {}, ID: {}", shooter.getName().getString(), this.getId());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        Entity owner = this.getOwner();

        // ===== 伤害逻辑（只在服务端执行） =====
        if (!this.level().isClientSide()) {
            if (NullPointerMod.ENABLE_DAMAGE && target instanceof LivingEntity living) {
                // 特殊处理：如果是玩家，不造成伤害（玩家由NPE崩溃机制处理）
                if (target instanceof Player) {
                    NullPointerMod.LOGGER.info("空指针击中玩家 {}，不造成伤害（由NPE崩溃机制处理）", target.getName().getString());
                } else {
                    // 动态计算伤害：目标血量的 2 倍 + 额外 10 点，保证秒杀任何生物
                    float maxHealth = living.getMaxHealth();
                    float damage = Math.max(maxHealth * 2.0f + 10.0f, 100.0f);
                    living.hurt(this.damageSources().thrown(this, owner), damage);

                    // 击退效果（像被踢飞一样）
                    double knockback = 3.0;
                    living.push(
                            (living.getX() - this.getX()) * knockback,
                            0.8,
                            (living.getZ() - this.getZ()) * knockback
                    );

                    NullPointerMod.LOGGER.info("空指针秒杀了 {}（血量: {}, 造成伤害: {})",
                            target.getName().getString(), maxHealth, damage);
                }
            } else if (!NullPointerMod.ENABLE_DAMAGE) {
                NullPointerMod.LOGGER.debug("实体伤害已关闭，未造成伤害");
            }
        }
        // ===== 伤害逻辑结束 =====

        // ===== 自爆：击中自己 → 无论开关都崩溃 =====
        if (target == owner) {
            NullPointerMod.LOGGER.warn("空指针击中了自己！发射者: {}", owner != null ? owner.getName().getString() : "null");
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
        // ===== 自爆逻辑结束 =====

        this.hitEntity = true;
        NullPointerMod.LOGGER.info("标记 hitEntity = true (击中实体: {})", target.getName().getString());
        super.onHitEntity(result);
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.BLOCK) {
            this.hitBlock = true;
            NullPointerMod.LOGGER.info("空指针击中方块，坐标: {}, {}, {}",
                    result.getLocation().x, result.getLocation().y, result.getLocation().z);
        } else {
            NullPointerMod.LOGGER.info("空指针击中其他: {}", result.getType());
        }
        super.onHit(result);
    }

    @Override
    public void tick() {
        super.tick();

        // 每 20 tick 记录一次状态（1秒）
        if (this.tickCount % 20 == 0) {
            NullPointerMod.LOGGER.info("空指针状态 - tick: {}, hitEntity: {}, hitBlock: {}, 位置: ({}, {}, {})",
                    this.tickCount, this.hitEntity, this.hitBlock,
                    String.format("%.2f", this.getX()), String.format("%.2f", this.getY()), String.format("%.2f", this.getZ()));
        }

        // ===== 崩溃条件检测 =====
        if ((this.tickCount > 200 || hitBlock) && !hitEntity) {
            Level level = this.level();
            NullPointerMod.LOGGER.warn("空指针触发崩溃条件 - tickCount: {}, hitBlock: {}, hitEntity: {}",
                    this.tickCount, this.hitBlock, this.hitEntity);

            if (level.isClientSide()) {
                NullPointerMod.LOGGER.error("客户端抛出 NPE");
                throw new NullPointerException("A wild NullPointerException appears! (Client tick)");
            }

            Entity owner = this.getOwner();
            if (owner instanceof ServerPlayer player) {
                NullPointerMod.LOGGER.warn("服务端发送崩溃指令给玩家: {}", player.getName().getString());
                NullPointerMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                        new ClientboundCrashPacket("You hit a block instead of an entity!")
                );
            } else {
                NullPointerMod.LOGGER.warn("未找到发射者，无法发送崩溃指令");
            }
            this.discard();
        }
        // ===== 崩溃条件检测结束 =====
    }

    @Override
    protected void defineSynchedData() {
        // 无数据同步
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
