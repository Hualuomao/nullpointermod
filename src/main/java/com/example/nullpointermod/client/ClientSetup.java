package com.example.nullpointermod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;

import com.example.nullpointermod.entity.ModEntities;

/**
 * Client-side setup and event subscriptions
 */
@Mod.EventBusSubscriber(modid = "nullpointermod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Register custom renderer for NullPointerProjectile
        event.registerEntityRenderer(ModEntities.NULL_POINTER_PROJECTILE.get(), 
            context -> new NullPointerProjectileRenderer(context));
    }

    public static void register() {
        // Additional client setup if needed
    }
}
