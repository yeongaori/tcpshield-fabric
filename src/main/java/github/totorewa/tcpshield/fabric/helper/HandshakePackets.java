package github.totorewa.tcpshield.fabric.helper;

import github.totorewa.tcpshield.fabric.api.network.HandshakePacket;
import github.totorewa.tcpshield.fabric.impl.network.HandshakePacketAdapter;
import github.totorewa.tcpshield.fabric.impl.network.ReflectedHandshakePacket;
import github.totorewa.tcpshield.fabric.mixin.packet.ClientIntentionPacketAccessor;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;

public final class HandshakePackets {
    private HandshakePackets() {
    }

    public static HandshakePacket wrap(ClientIntentionPacket packet) {
        if (McVersion.LEGACY_HANDSHAKE_PACKET) {
            // The cast only succeeds when the accessor mixin was applied, which the
            // mixin plugin guarantees on 1.20/1.20.1.
            return new HandshakePacketAdapter((ClientIntentionPacketAccessor) (Object) packet);
        }
        return new ReflectedHandshakePacket(packet);
    }
}
