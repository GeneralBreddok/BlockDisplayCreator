package me.general_breddok.blockdisplaycreator.version;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;


/**
 * Enum representing different Minecraft versions.
 * Each version is associated with a string representation.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MinecraftVersion {
    V1_19_4("1.19.4"),
    V1_20("1.20"),
    V1_20_1("1.20.1"),
    V1_20_2("1.20.2"),
    V1_20_3("1.20.3"),
    V1_20_4("1.20.4"),
    V1_20_5("1.20.5"),
    V1_20_6("1.20.6"),
    V1_21("1.21"),
    V1_21_1("1.21.1"),
    V1_21_2("1.21.2"),
    V1_21_3("1.21.3"),
    V1_21_4("1.21.4"),
    V1_21_5("1.21.5"),
    V1_21_6("1.21.6"),
    V1_21_7("1.21.7"),
    V1_21_8("1.21.8"),
    V1_21_9("1.21.9"),
    V1_21_10("1.21.10"),
    V1_21_11("1.21.11");

    String line;

    /**
     * Returns the MinecraftVersion enum corresponding to the given version string.
     * If no match is found, returns null.
     *
     * @param versionString The version string to match.
     * @return The corresponding MinecraftVersion or null if not found.
     */
    @Nullable
    public static MinecraftVersion fromString(String versionString) {
        for (MinecraftVersion version : values()) {
            if (versionString.equals(version.getLine())) {
                return version;
            }
        }
        return null;
    }

    /**
     * Checks if this MinecraftVersion is within the range defined by the two bounds.
     *
     * @param bound1 The first bound of the range.
     * @param bound2 The second bound of the range.
     * @return true if this version is within the range, false otherwise.
     */
    public boolean isInRange(MinecraftVersion bound1, MinecraftVersion bound2) {
        int min = Math.min(bound1.ordinal(), bound2.ordinal());
        int max = Math.max(bound1.ordinal(), bound2.ordinal());
        int target = this.ordinal();
        return target >= min && target <= max;
    }

    /**
     * Checks if this MinecraftVersion is at least the specified version.
     *
     * @param other The version to compare against.
     * @return true if this version is at least the specified version, false otherwise.
     */
    public boolean isAtLeast(MinecraftVersion other) {
        return this.ordinal() >= other.ordinal();
    }

    /**
     * Checks if this MinecraftVersion is before the specified version.
     *
     * @param other The version to compare against.
     * @return true if this version is before the specified version, false otherwise.
     */
    public boolean isBelow(MinecraftVersion other) {
        return this.ordinal() < other.ordinal();
    }

    /**
     * Returns the next MinecraftVersion in the enum, or null if this is the last version.
     *
     * @return The next MinecraftVersion or null if there is no next version.
     */
    @Nullable
    public MinecraftVersion getNext() {
        int nextIndex = this.ordinal() + 1;
        return nextIndex < values().length
                ? values()[nextIndex]
                : null;
    }

    /**
     * Returns the previous MinecraftVersion in the enum, or null if this is the first version.
     *
     * @return The previous MinecraftVersion or null if there is no previous version.
     */
    @Nullable
    public MinecraftVersion getPrevious() {
        int prevIndex = this.ordinal() - 1;
        return prevIndex >= 0
                ? values()[prevIndex]
                : null;
    }
}
