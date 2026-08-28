package com.example.nullpointermod.entity;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import com.example.nullpointermod.NullPointerMod;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NullPointerMod.MOD_ID);

    public static final RegistryObject<EntityType<NullPointerProjectile>> NULL_POINTER_PROJECTILE = 
        ENTITY_TYPES.register("null_pointer_projectile",
            () -> EntityType.Builder.<NullPointerProjectile>of(NullPointerProjectile::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .clientTrackingRange(4)
                .updateInterval(20)
                .build("nullpointermod:null_pointer_projectile"));
}
