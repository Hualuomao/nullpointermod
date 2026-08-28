package com.example.nullpointermod.network;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import com.example.nullpointermod.NullPointerMod;

public class ModNetwork {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new net.minecraft.resources.ResourceLocation(NullPointerMod.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        
        INSTANCE.messageBuilder(ClientboundCrashPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ClientboundCrashPacket::fromBytes)
            .encoder(ClientboundCrashPacket::toBytes)
            .consumerMainThread(ClientboundCrashPacket::handle)
            .add();
    }
}
