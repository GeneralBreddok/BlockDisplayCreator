package me.general_breddok.blockdisplaycreator.version;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VersionedKey {
    private static final Pattern VERSIONED_PATTERN =
            Pattern.compile("^(?<key>.+?)-(?<from>\\d+_\\d+(?:_\\d+)?)(?:-(?<to>\\d+_\\d+(?:_\\d+)?))?$");
    String baseName;
    MinecraftVersion minVersion;
    MinecraftVersion maxVersion;

    public static VersionedKey parse(String input) {
        Matcher matcher = VERSIONED_PATTERN.matcher(input);
        if (!matcher.matches()) {
            return null;
        }

        String key = matcher.group("key");
        String fromStr = matcher.group("from").replace('_', '.');
        String toStr = matcher.group("to") != null ? matcher.group("to").replace('_', '.') : null;

        MinecraftVersion min = MinecraftVersion.fromString(fromStr);
        MinecraftVersion max = toStr != null ? MinecraftVersion.fromString(toStr) : min;

        if (min == null || max == null) {
            return null;
        }

        return new VersionedKey(key, min, max);
    }

    public boolean appliesTo(MinecraftVersion version) {
        return version.ordinal() >= minVersion.ordinal() && version.ordinal() <= maxVersion.ordinal();
    }
}
