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

    public static String CUSTOM_BLOCK_PLACE_FAIL_REASON_SOLID_BLOCK;
    public static String CUSTOM_BLOCK_PLACE_FAIL_REASON_CHUNK_NOT_LOADED;
    public static String CUSTOM_BLOCK_PLACE_FAIL_REASON_CUSTOM_BLOCK_PRESENT;

    public static String CUSTOM_BLOCK_BREAK_FAIL_REASON_NO_CUSTOM_BLOCK;

    public static String CUSTOM_BLOCK_NOT_FOUND;

    public static String COMMAND_BLOCK_NOT_EXISTS;
    public static String COMMAND_RELOAD;
    public static String COMMAND_RELOAD_BLOCK;

    public static String COMMAND_CUSTOM_BLOCK_GIVE_PLAYER_RECEIVE;
    public static String COMMAND_CUSTOM_BLOCK_GIVE_PLAYER;
    public static String COMMAND_CUSTOM_BLOCK_GIVE_PLAYERS;
    public static String COMMAND_CUSTOM_BLOCK_GIVE_NO_PLAYER;

    public static String COMMAND_CUSTOM_BLOCK_PLACE_PLACED;
    public static String COMMAND_CUSTOM_BLOCK_PLACE_INVALID_ATTACHED_FACE;
    public static String COMMAND_CUSTOM_BLOCK_PLACE_FAILED;
    public static String COMMAND_CUSTOM_BLOCK_PLACE_FAILED_WITHOUT_REASON;
    public static String COMMAND_CUSTOM_BLOCK_PLACE_OPTION_DESCRIPTION_REPLACE_CUSTOM_BLOCK;
    public static String COMMAND_CUSTOM_BLOCK_PLACE_OPTION_DESCRIPTION_LOAD_CHUNK;
    public static String COMMAND_CUSTOM_BLOCK_PLACE_OPTION_DESCRIPTION_BREAK_SOLID_MATERIAL;
    public static String COMMAND_CUSTOM_BLOCK_PLACE_OPTION_DESCRIPTION_SILENT_PLACE;

    public static String COMMAND_CUSTOM_BLOCK_BREAK_BROKEN;
    public static String COMMAND_CUSTOM_BLOCK_BREAK_NO_CUSTOM_BLOCK;
    public static String COMMAND_CUSTOM_BLOCK_BREAK_OPTION_DESCRIPTION_DROP_ITEM;
    public static String COMMAND_CUSTOM_BLOCK_BREAK_OPTION_DESCRIPTION_SILENT_BREAK;

    public static String COMMAND_CUSTOM_BLOCK_EDITFILE_SET;

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

        CUSTOM_BLOCK_PLACE_FAIL_REASON_SOLID_BLOCK = configuration.get("custom-block.place.fail-reason.solid-block",
                "Block at location %x% %y% %z% in world %world% is solid and break_solid_material option is not set.");
        CUSTOM_BLOCK_PLACE_FAIL_REASON_CHUNK_NOT_LOADED = configuration.get("custom-block.place.fail-reason.chunk-not-loaded",
                "Location %x% %y% %z% in world %world% is not loaded and load_chunk option is not set.");
        CUSTOM_BLOCK_PLACE_FAIL_REASON_CUSTOM_BLOCK_PRESENT = configuration.get("custom-block.place.fail-reason.custom-block-present",
                "A custom block is already present at location %x% %y% %z% in world %world% and replace_custom_block option is not set.");

        CUSTOM_BLOCK_BREAK_FAIL_REASON_NO_CUSTOM_BLOCK = configuration.get("custom-block.break.fail-reason.no-custom-block",
                "&cThere is no custom block to break at location %x% %y% %z% in world %world%.");

        CUSTOM_BLOCK_NOT_FOUND = configuration.get("custom-block.not-found",
                "&cCustom block %customblock_name% not found! Perhaps it was deleted or was not loaded correctly.");

        COMMAND_BLOCK_NOT_EXISTS = configuration.get("command.block-not-exists", "&cBlock %customblock_name% does not exist!");
        COMMAND_RELOAD = configuration.get("command.reload", "&aConfig has been reloaded!");
        COMMAND_RELOAD_BLOCK = configuration.get("command.reload-block", "&aBlock %customblock_name% has been reloaded!");

        COMMAND_CUSTOM_BLOCK_GIVE_PLAYER_RECEIVE = configuration.get("command.custom-block.give.player-receive",
                "&aYou have received the &b%customblock_name%&ox&b%amount%&a block");
        COMMAND_CUSTOM_BLOCK_GIVE_PLAYER = configuration.get("command.custom-block.give.give-player",
                "&aBlock &b%customblock_name%&ox&b%amount%&a was successfully given to the player &b%player%&a!");
        COMMAND_CUSTOM_BLOCK_GIVE_PLAYERS = configuration.get("command.custom-block.give.give-players",
                "&aBlock &b%customblock_name%&ox&b%amount%&a was successfully given!");
        COMMAND_CUSTOM_BLOCK_GIVE_NO_PLAYER = configuration.get("command.custom-block.give.no-player",
                "&cYou have not specified the block receiver!");

        COMMAND_CUSTOM_BLOCK_PLACE_PLACED = configuration.get("command.custom-block.place.placed",
                "&aSuccessfully placed the block &b%customblock_name%&r&a at location &b%x% %y% %z% &ain the world &b%world%&a!");
        COMMAND_CUSTOM_BLOCK_PLACE_INVALID_ATTACHED_FACE = configuration.get("command.custom-block.place.invalid-attached-face",
                "&cInvalid attached face: %face%. Valid values are: north, south, east, west, up, down");
        COMMAND_CUSTOM_BLOCK_PLACE_FAILED = configuration.get("command.custom-block.place.failed",
                "&cFailed to place the block %customblock_name%: %reason%");
        COMMAND_CUSTOM_BLOCK_PLACE_FAILED_WITHOUT_REASON = configuration.get("command.custom-block.place.failed-without-reason",
                "&cFailed to place the block &b%customblock_name%&r&c at &b%x% %y% %z% &cin the world &b%world%&c!");

        COMMAND_CUSTOM_BLOCK_PLACE_OPTION_DESCRIPTION_REPLACE_CUSTOM_BLOCK = configuration.get("command.custom-block.place.option-description.replace-custom-block",
                "&eReplaces an existing custom block at the target location");
        COMMAND_CUSTOM_BLOCK_PLACE_OPTION_DESCRIPTION_LOAD_CHUNK = configuration.get("command.custom-block.place.option-description.load-chunk",
                "&eLoads the chunk before placing the block, if needed");
        COMMAND_CUSTOM_BLOCK_PLACE_OPTION_DESCRIPTION_BREAK_SOLID_MATERIAL = configuration.get("command.custom-block.place.option-description.break-solid-material",
                "&eDestroys a solid vanilla block at the target location, if present");
        COMMAND_CUSTOM_BLOCK_PLACE_OPTION_DESCRIPTION_SILENT_PLACE = configuration.get("command.custom-block.place.option-description.silent-place",
                "&eSuppresses block placement sounds");

        COMMAND_CUSTOM_BLOCK_BREAK_BROKEN = configuration.get("command.custom-block.break.broken",
                "&aSuccessfully broke the block &b%customblock_name%&r&a at location &b%x% %y% %z% &ain the world &b%world%");
        COMMAND_CUSTOM_BLOCK_BREAK_NO_CUSTOM_BLOCK = configuration.get("command.custom-block.break.no-custom-block",
                "&cNo custom block found at &4%x% %y% %z% &cin the world &4%world%&c!");

        COMMAND_CUSTOM_BLOCK_BREAK_OPTION_DESCRIPTION_DROP_ITEM = configuration.get("command.custom-block.break.option-description.drop-item",
                "&eDrops the corresponding item when breaking the block");
        COMMAND_CUSTOM_BLOCK_BREAK_OPTION_DESCRIPTION_SILENT_BREAK = configuration.get("command.custom-block.break.option-description.silent-break",
                "&eSuppresses block breaking sounds");

        COMMAND_CUSTOM_BLOCK_EDITFILE_SET = configuration.get("command.custom-block.editfile.set", "&aBlock &b%customblock_name%&a parameter &b%parameter%&a was successfully set to \"&b%value%&a\".");
    }
}
