package com.example.nullpointermod;

import com.example.nullpointermod.command.NullPointerCommand;
import com.example.nullpointermod.entity.NullPointerProjectile;
import com.example.nullpointermod.item.JavaItem;
import com.example.nullpointermod.item.NullPointerItem;
import com.example.nullpointermod.network.ClientboundCrashPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(NullPointerMod.MOD_ID)
public class NullPointerMod {
    public static final String MOD_ID = "nullpointermod";
    public static final Logger LOGGER = LoggerFactory.getLogger(NullPointerMod.class);

    // ===== 实体伤害开关（默认关闭） =====
    public static boolean ENABLE_DAMAGE = false;

    // ===== 网络通道 =====
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    // ===== 注册物品 =====
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> NULL_POINTER_ITEM = ITEMS.register("java_null_pointer_exception",
            NullPointerItem::new);
    public static final RegistryObject<Item> JAVA_ITEM = ITEMS.register("java_item",
            JavaItem::new);

    // ===== 注册实体 =====
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);
    public static final RegistryObject<EntityType<NullPointerProjectile>> NULL_POINTER_PROJECTILE =
            ENTITIES.register("null_pointer_projectile",
                    () -> EntityType.Builder.<NullPointerProjectile>of(NullPointerProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("null_pointer_projectile")
            );

    // ===== 注册创造模式标签页（只包含 Java 物品，NPE 不显示） =====
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> NULL_POINTER_TAB =
            CREATIVE_TABS.register("nullpointer_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup." + MOD_ID))
                            .icon(() -> new ItemStack(JAVA_ITEM.get()))
                            .displayItems((parameters, output) -> {
                                // ✅ 只添加 Java 物品，空指针物品不出现在创造模式
                                output.accept(JAVA_ITEM.get());
                                // output.accept(NULL_POINTER_ITEM.get()); // ❌ 故意注释掉
                            })
                            .build()
            );

    public NullPointerMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        CHANNEL.registerMessage(0, ClientboundCrashPacket.class,
                ClientboundCrashPacket::encode,
                ClientboundCrashPacket::decode,
                ClientboundCrashPacket::handle
        );

        MinecraftForge.EVENT_BUS.register(this);
    }

    // ===== 注册指令 =====
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        NullPointerCommand.register(event.getDispatcher());
        LOGGER.info("已注册 /nullpointer 指令");
    }
}
