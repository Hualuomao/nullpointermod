package com.example.nullpointermod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundCrashPacket {
    private final String message;

    public ClientboundCrashPacket(String message) {
        this.message = message;
    }

    public ClientboundCrashPacket(FriendlyByteBuf buf) {
        this(buf.readUtf(256));
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(message, 256);
    }

    public static ClientboundCrashPacket decode(FriendlyByteBuf buf) {
        return new ClientboundCrashPacket(buf);
    }

    public static void encode(ClientboundCrashPacket packet, FriendlyByteBuf buf) {
        packet.toBytes(buf);
    }

    public static void handle(ClientboundCrashPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            throw new NullPointerException("A wild NullPointerException appears! " + packet.message);
        });
        ctx.get().setPacketHandled(true);
    }
}
