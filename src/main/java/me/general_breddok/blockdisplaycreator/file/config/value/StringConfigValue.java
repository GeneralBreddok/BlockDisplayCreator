package me.general_breddok.blockdisplaycreator.file.config.value;

import me.general_breddok.blockdisplaycreator.data.yaml.YamlData;

public final class StringConfigValue implements ConstantConfigValue<String> {
    public static String MAX_ENTITIES_PER_CHUNK;
    public static String PERMISSION_DENIED_PLACE;
    public static String PERMISSION_DENIED_BREAK;
    public static String PERMISSION_DENIED_INTERACT;

    public static String REGION_DENIED_PLACE;
    public static String REGION_DENIED_BREAK;
    public static String REGION_DENIED_INTERACT;

    @Override
    public void initialize(YamlData<String> configuration) {
        MAX_ENTITIES_PER_CHUNK = configuration.get("max-entities-per-chunk", "&cYou cannot place a custom block in this chunk!");

        PERMISSION_DENIED_PLACE = configuration.get("permission-denied.place", "&cSorry, you do not have permission to place this block.");
        PERMISSION_DENIED_BREAK = configuration.get("permission-denied.break", "&cSorry, you do not have permission to break this block.");
        PERMISSION_DENIED_INTERACT = configuration.get("permission-denied.interact", "&cSorry, you do not have permission to interact with this block.");

        REGION_DENIED_PLACE = configuration.get("region-denied.place", "&cYou cannot place blocks in foreign regions.");
        REGION_DENIED_BREAK = configuration.get("region-denied.break", "&cYou cannot break blocks in foreign regions.");
        REGION_DENIED_INTERACT = configuration.get("region-denied.interact", "&cYou cannot interact with blocks in foreign regions.");
    }
}
