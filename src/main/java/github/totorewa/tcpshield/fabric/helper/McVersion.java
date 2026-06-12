package github.totorewa.tcpshield.fabric.helper;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;

public final class McVersion {
    /**
     * ClientIntentionPacket became a record in 1.20.2, which changed the
     * intermediary names of its members. The accessor mixin only fits 1.20
     * and 1.20.1; later versions go through reflection instead.
     */
    public static final boolean LEGACY_HANDSHAKE_PACKET = isBelow("1.20.2");

    private McVersion() {
    }

    private static boolean isBelow(String version) {
        try {
            Version minecraft = FabricLoader.getInstance().getModContainer("minecraft")
                    .orElseThrow(IllegalStateException::new)
                    .getMetadata().getVersion();
            return minecraft.compareTo(Version.parse(version)) < 0;
        } catch (Exception e) {
            // Snapshots and other unparseable versions are newer than 1.20.2
            return false;
        }
    }
}
