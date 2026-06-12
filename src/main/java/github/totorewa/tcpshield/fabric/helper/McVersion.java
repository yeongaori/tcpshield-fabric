package github.totorewa.tcpshield.fabric.helper;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;

public final class McVersion {
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
            return false;
        }
    }
}
