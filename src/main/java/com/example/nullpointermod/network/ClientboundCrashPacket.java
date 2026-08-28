package com.example.nullpointermod.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/**
 * Clientbound Crash Packet
 * Server sends this to client to trigger a NullPointerException crash
 */
public class ClientboundCrashPacket {

    private String crashReason;

    public ClientboundCrashPacket(String reason) {
        this.crashReason = reason;
    }

    public ClientboundCrashPacket() {
        this("Unknown reason");
    }

    public void toBytes(net.minecraft.network.FriendlyByteBuf buf) {
        buf.writeUtf(crashReason);
    }

    public static ClientboundCrashPacket fromBytes(net.minecraft.network.FriendlyByteBuf buf) {
        return new ClientboundCrashPacket(buf.readUtf());
    }

    public static boolean handle(ClientboundCrashPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                // Trigger NullPointerException on client side
                throw new NullPointerException("Crash triggered by packet: " + packet.crashReason);
            });
        });

        return true;
    }
}
