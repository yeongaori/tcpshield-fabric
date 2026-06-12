package github.totorewa.tcpshield.fabric.mixin.packet;

import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Only applied on 1.20/1.20.1 (see TcpShieldMixinPlugin); the packet is a
 * record on 1.20.2+ where ReflectedHandshakePacket takes over.
 */
@Mixin(ClientIntentionPacket.class)
public interface ClientIntentionPacketAccessor {
    @Accessor("hostName")
    String getHostName();

    @Mutable
    @Accessor("hostName")
    void setHostName(String hostName);
}
