package github.totorewa.tcpshield.fabric.impl.network;

import github.totorewa.tcpshield.fabric.api.network.HandshakePacket;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;

import java.lang.reflect.Field;
import java.util.logging.Logger;

/**
 * Hostname access for 1.20.2+, where ClientIntentionPacket is a record and the
 * accessor mixin cannot be applied. The packet declares exactly one String
 * field (hostName), so the lookup survives mapping changes between versions.
 */
public class ReflectedHandshakePacket implements HandshakePacket {
    private static final Field HOST_NAME_FIELD = resolveHostNameField();

    private final ClientIntentionPacket packet;

    public ReflectedHandshakePacket(ClientIntentionPacket packet) {
        this.packet = packet;
    }

    @Override
    public String getHostName() {
        if (HOST_NAME_FIELD == null) return "";
        try {
            return (String) HOST_NAME_FIELD.get(this.packet);
        } catch (IllegalAccessException e) {
            // Unreachable after setAccessible(true); treat as missing payload so
            // only-allow-proxy-connections fails closed.
            return "";
        }
    }

    @Override
    public void setHostName(String hostname) {
        // Record fields cannot be written reflectively. The leftover payload in
        // the packet's hostname is ignored by the vanilla server, so skipping the
        // rewrite is harmless here.
    }

    private static Field resolveHostNameField() {
        Field match = null;
        for (Field field : ClientIntentionPacket.class.getDeclaredFields()) {
            if (field.getType() == String.class) {
                if (match != null) {
                    match = null; // ambiguous: a newer version added another String field
                    break;
                }
                match = field;
            }
        }

        if (match == null) {
            Logger.getLogger("TCPShield").severe(
                    "Could not locate the hostname field in ClientIntentionPacket. " +
                    "TCPShield cannot read proxy payloads on this Minecraft version; " +
                    "connections will be rejected while only-allow-proxy-connections is enabled.");
            return null;
        }

        match.setAccessible(true);
        return match;
    }
}
