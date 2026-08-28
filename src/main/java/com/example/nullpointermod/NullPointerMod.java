package com.example.nullpointermod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafxmod.FMLJavaModLoadingContext;

import com.example.nullpointermod.client.ClientSetup;
import com.example.nullpointermod.item.JavaItem;
import com.example.nullpointermod.item.NullPointerItem;
import com.example.nullpointermod.entity.NullPointerProjectile;
import com.example.nullpointermod.network.ClientboundCrashPacket;

@Mod("nullpointermod")
public class NullPointerMod {

    public static final String MOD_ID = "nullpointermod";

    public NullPointerMod() {
        // Register setup events
        IEventBus modEventBus = FMLJavaModLoadingContext.getInstance().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Register items
        // Register entities
        // Register network packets
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Register client-side renderers and handlers
        ClientSetup.register();
    }
}
