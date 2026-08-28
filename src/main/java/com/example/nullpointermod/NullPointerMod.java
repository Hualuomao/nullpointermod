package com.example.nullpointermod;

import com.example.nullpointermod.entity.NullPointerProjectile;
import com.example.nullpointermod.item.JavaItem;
import com.example.nullpointermod.item.NullPointerItem;
import com.example.nullpointermod.network.ClientboundCrashPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(NullPointerMod.MOD_ID)
public class NullPointerMod {
    public static final String MOD_ID = "nullpointermod";
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> NULL_POINTER_ITEM = ITEMS.register("java_null_pointer_exception", NullPointerItem::new);
    public static final RegistryObject<Item> JAVA_ITEM = ITEMS.register("java_item", JavaItem::new);

    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);
    public static final RegistryObject<EntityType<NullPointerProjectile>> NULL_POINTER_PROJECTILE =
            ENTITIES.register("null_pointer_projectile",
                    () -> EntityType.Builder.<NullPointerProjectile>of(NullPointerProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("null_pointer_projectile")
            );

    public NullPointerMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);

        CHANNEL.registerMessage(0, ClientboundCrashPacket.class,
                ClientboundCrashPacket::encode,
                ClientboundCrashPacket::decode,
                ClientboundCrashPacket::handle
        );

        MinecraftForge.EVENT_BUS.register(this);
    }

    // ---------- 定时扫描容器，销毁非法存放的空指针 ----------
    private static int tickCounter = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;
            if (tickCounter % 20 == 0) {
                scanAndCleanContainers();
            }
        }
    }

    private void scanAndCleanContainers() {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) return;

        for (ServerLevel level : server.getAllLevels()) {
            for (BlockEntity be : level.getBlockEntities().values()) {
                if (be instanceof EnderChestBlockEntity) continue;
                if (be instanceof Container container) {
                    for (int slot = 0; slot < container.getContainerSize(); slot++) {
                        ItemStack stack = container.getItem(slot);
                        if (!stack.isEmpty() && stack.getItem() == NULL_POINTER_ITEM.get()) {
                            container.setItem(slot, ItemStack.EMPTY);
                            // 可选：日志
                            // NullPointerMod.LOGGER.warn("Destroyed NullPointer at {}", be.getBlockPos());
                        }
                    }
                }
            }
        }
    }
}
