package me.general_breddok.blockdisplaycreator.version;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Server;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VersionManager {
    Server server;
    MinecraftVersion currentVersion;

    public VersionManager(Server server) {
        this.server = server;
        this.currentVersion = resolveVersion(server);
    }

    private MinecraftVersion resolveVersion(Server server) {
        String versionString = extractVersion(server.getBukkitVersion());
        MinecraftVersion minecraftVersion = MinecraftVersion.fromString(versionString);

        if (minecraftVersion == null) {
            throw new IllegalArgumentException("Unsupported Minecraft version: " + versionString);
        }

        return minecraftVersion;
    }

    private String extractVersion(String bukkitVersion) {
        return bukkitVersion.split("-")[0];
    }

    public boolean isVersionBefore1_20_5() {
        return this.currentVersion.isBelow(MinecraftVersion.V1_20_5);
    }

    public boolean isVersionBefore1_21_3() {
        return this.currentVersion.isBelow(MinecraftVersion.V1_21_3);
    }

    public boolean isVersionBefore1_21_5() {
        return this.currentVersion.isBelow(MinecraftVersion.V1_21_5);
    }

    public boolean isVersion1_19_4() {
        return this.currentVersion.equals(MinecraftVersion.V1_19_4);
    }
}
