package github.totorewa.tcpshield.fabric.impl.network;

import github.totorewa.tcpshield.fabric.api.network.HandshakePacket;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;

import java.lang.reflect.Field;
import java.util.logging.Logger;

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
            return "";
        }
    }

    @Override
    public void setHostName(String hostname) {
    }

    private static Field resolveHostNameField() {
        Field match = null;
        for (Field field : ClientIntentionPacket.class.getDeclaredFields()) {
            if (field.getType() == String.class) {
                if (match != null) {
                    match = null;
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
