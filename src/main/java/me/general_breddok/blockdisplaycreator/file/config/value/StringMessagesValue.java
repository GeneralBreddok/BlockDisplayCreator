package me.general_breddok.blockdisplaycreator.file.config.value;

import me.general_breddok.blockdisplaycreator.data.yaml.YamlData;

public final class StringMessagesValue implements ConstantConfigValue<String> {
    public static String MAX_ENTITIES_PER_CHUNK;
    public static String PERMISSION_DENIED_PLACE;
    public static String PERMISSION_DENIED_BREAK;
    public static String PERMISSION_DENIED_INTERACT;

    public static String REGION_DENIED_PLACE;
    public static String REGION_DENIED_BREAK;
    public static String REGION_DENIED_INTERACT;

    public static String ISLAND_DENIED_PLACE;
    public static String ISLAND_DENIED_BREAK;
    public static String ISLAND_DENIED_INTERACT;

    @Override
    public void initialize(YamlData<String> configuration) {
        MAX_ENTITIES_PER_CHUNK = configuration.get("max-entities-per-chunk", "&cYou cannot place a custom block in this chunk!");

        PERMISSION_DENIED_PLACE = configuration.get("permission-denied.place", "&cYou do not have permission to place this block.");
        PERMISSION_DENIED_BREAK = configuration.get("permission-denied.break", "&cYou do not have permission to break this block.");
        PERMISSION_DENIED_INTERACT = configuration.get("permission-denied.interact", "&cYou do not have permission to interact with this block.");

        REGION_DENIED_PLACE = configuration.get("region-denied.place", "&c&lHey! &r&7Sorry, but you can`t place that custom block here.");
        REGION_DENIED_BREAK = configuration.get("region-denied.break", "&c&lHey! &r&7Sorry, but you can`t break that custom block here.");
        REGION_DENIED_INTERACT = configuration.get("region-denied.interact", "&c&lHey! &r&7Sorry, but you can`t interact with that custom block here.");

        ISLAND_DENIED_PLACE = configuration.get("island-denied.place", "&c&lError | &7This island is protected. You cannot place custom blocks here.");
        ISLAND_DENIED_BREAK = configuration.get("island-denied.break", "&c&lError | &7This island is protected. You cannot break custom blocks here.");
        ISLAND_DENIED_INTERACT = configuration.get("island-denied.interact", "&c&lError | &7This island is protected. You cannot interact with custom blocks here.");
    }
}
