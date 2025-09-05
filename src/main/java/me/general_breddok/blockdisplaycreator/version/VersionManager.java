package me.general_breddok.blockdisplaycreator.version;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Server;

/**
 * Utility class for detecting and comparing the current Minecraft server version.
 * <p>
 * Extracts the version from the running {@link Server} instance and provides
 * convenience checks for specific supported versions.
 * </p>
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VersionManager {
    /**
     * Reference to the running server.
     */
    Server server;
    /**
     * Parsed Minecraft version of the server.
     */
    MinecraftVersion currentVersion;

    /**
     * Creates a new version manager for the given server.
     *
     * @param server the running server instance
     * @throws IllegalArgumentException if the server version cannot be resolved
     */
    public VersionManager(Server server) {
        this.server = server;
        this.currentVersion = resolveVersion(server);
    }

    /**
     * Resolves the current Minecraft version from the server.
     *
     * @param server the server instance
     * @return the parsed {@link MinecraftVersion}
     * @throws IllegalArgumentException if the version string is not supported
     */
    private MinecraftVersion resolveVersion(Server server) throws IllegalArgumentException {
        String versionString = extractVersion(server.getBukkitVersion());
        MinecraftVersion minecraftVersion = MinecraftVersion.fromString(versionString);

        if (minecraftVersion == null) {
            throw new IllegalArgumentException("Unsupported Minecraft version: " + versionString);
        }

        return minecraftVersion;
    }

    /**
     * Extracts the raw Minecraft version string from a Bukkit version string.
     *
     * @param bukkitVersion the Bukkit version string
     * @return the extracted Minecraft version (e.g. "1.20.1")
     */
    private String extractVersion(String bukkitVersion) {
        return bukkitVersion.split("-")[0];
    }

    /**
     * Checks if the server is running a version earlier than 1.20.5.
     *
     * @return {@code true} if current version is below 1.20.5
     */
    public boolean isVersionBelow1_20_5() {
        return this.currentVersion.isBelow(MinecraftVersion.V1_20_5);
    }

    /**
     * Checks if the server is running a version earlier than 1.21.3.
     *
     * @return {@code true} if current version is below 1.21.3
     */
    public boolean isVersionBelow1_21_3() {
        return this.currentVersion.isBelow(MinecraftVersion.V1_21_3);
    }

    /**
     * Checks if the server is running a version earlier than 1.21.5.
     *
     * @return {@code true} if current version is below 1.21.5
     */
    public boolean isVersionBelow1_21_5() {
        return this.currentVersion.isBelow(MinecraftVersion.V1_21_5);
    }

    /**
     * Checks if the server is running exactly Minecraft 1.19.4.
     *
     * @return {@code true} if current version is 1.19.4
     */
    public boolean isVersion1_19_4() {
        return this.currentVersion.equals(MinecraftVersion.V1_19_4);
    }
}
