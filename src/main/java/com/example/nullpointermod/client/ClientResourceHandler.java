package com.example.nullpointermod.client;

import com.example.nullpointermod.NullPointerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = NullPointerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientResourceHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                // 强制重新加载资源
                Minecraft.getInstance().reloadResourcePacks();
                NullPointerMod.LOGGER.info("Resources reloaded for Android compatibility!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
