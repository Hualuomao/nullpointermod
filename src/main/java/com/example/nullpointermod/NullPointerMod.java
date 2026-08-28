package com.example.nullpointermod;

import com.example.nullpointermod.entity.NullPointerProjectile;
import com.example.nullpointermod.item.JavaItem;
import com.example.nullpointermod.item.NullPointerItem;
import com.example.nullpointermod.network.ClientboundCrashPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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
}
