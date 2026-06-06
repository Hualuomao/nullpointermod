package com.example.nullpointermod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(NullPointerMod.MODID)
public class NullPointerMod {
    public static final String MODID = "nullpointermod";

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<Item> NULL_POINTER_ITEM =
            ITEMS.register("java_null_pointer_exception",
                    () -> new NullPointerItem(new Item.Properties()
                            .stacksTo(64)));  // 可堆叠 64，方便测试

    public static final RegistryObject<EntityType<NullPointerProjectile>> NULL_POINTER_PROJECTILE =
            ENTITY_TYPES.register("null_pointer_projectile",
                    () -> EntityType.Builder.<NullPointerProjectile>of(NullPointerProjectile::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)   // 增大碰撞箱，提高命中率
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("null_pointer_projectile"));

    public NullPointerMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        // 注册事件监听，用于将物品添加到创造模式物品栏
        modEventBus.register(this);
    }

    @SubscribeEvent
    public void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        // 将物品添加到“原材料”标签页
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(NULL_POINTER_ITEM.get());
        }
    }
}