package me.general_breddok.blockdisplaycreator.permission;

public interface DefaultPermissions {
    interface BDC {

        String WG_BYPASS = "bdc.bypass.wg";

        interface CustomBlock {
            String BLOCKS_DESTRUCTION = "blockdisplaycreator.blocks-destruction";
            String BLOCKS_PLACEMENT = "blockdisplaycreator.blocks-placement";
            String BLOCKS_INTERACTION = "blockdisplaycreator.blocks-interaction";
        }

        interface Command {
            String CUSTOM_BLOCK = "bdc.command.custom-block";
        }
    }

    interface Vanilla {
        String MINECRAFT = "minecraft";
        interface Command {
            String COMMAND = ".command";
            String SUMMON = MINECRAFT + COMMAND + ".summon";
            String FUNCTION = MINECRAFT + COMMAND + ".function";
            String PLAYSOUND = MINECRAFT + COMMAND + ".playsound";
            String BAN = MINECRAFT + COMMAND + ".ban";
            String BAN_IP = MINECRAFT + COMMAND + ".ban-ip";
            String BANLIST = MINECRAFT + COMMAND + ".banlist";
            String DATA = MINECRAFT + COMMAND + ".data";
            String DATAPACK = MINECRAFT + COMMAND + ".datapack";
            String DEBUG = MINECRAFT + COMMAND + ".debug";
            String DIFFICULTY = MINECRAFT + COMMAND + ".difficulty";
            String CLEAR = MINECRAFT + COMMAND + ".clear";
            String CLONE = MINECRAFT + COMMAND + ".clone";
            String DEFAULTGAMEMODE = MINECRAFT + COMMAND + ".defaultgamemode";
            String DEOP = MINECRAFT + COMMAND + ".deop";
            String EFFECT = MINECRAFT + COMMAND + ".effect";
            String ENCHANT = MINECRAFT + COMMAND + ".enchant";
            String EXECUTE = MINECRAFT + COMMAND + ".execute";
            String FILL = MINECRAFT + COMMAND + ".fill";
            String GAMEMODE = MINECRAFT + COMMAND + ".gamemode";
            String GAMERULE = MINECRAFT + COMMAND + ".gamerule";
            String GIVE = MINECRAFT + COMMAND + ".give";
            String HELP = MINECRAFT + COMMAND + ".help";
            String KICK = MINECRAFT + COMMAND + ".kick";
            String KILL = MINECRAFT + COMMAND + ".kill";
            String LIST = MINECRAFT + COMMAND + ".list";
            String LOCATE = MINECRAFT + COMMAND + ".locate";
            String ME = MINECRAFT + COMMAND + ".me";
            String MSG = MINECRAFT + COMMAND + ".msg";
            String OP = MINECRAFT + COMMAND + ".op";
            String PARDON = MINECRAFT + COMMAND + ".pardon";
            String PARDON_IP = MINECRAFT + COMMAND + ".pardon-ip";
            String PARTICLE = MINECRAFT + COMMAND + ".particle";
            String RELOAD = MINECRAFT + COMMAND + ".reload";
            String SAY = MINECRAFT + COMMAND + ".say";
            String SCOREBOARD = MINECRAFT + COMMAND + ".scoreboard";
            String SEED = MINECRAFT + COMMAND + ".seed";
            String SETBLOCK = MINECRAFT + COMMAND + ".setblock";
            String SETWORLDSPAWN = MINECRAFT + COMMAND + ".setworldspawn";
            String SPAWNPOINT = MINECRAFT + COMMAND + ".spawnpoint";
            String SPREADPLAYERS = MINECRAFT + COMMAND + ".spreadplayers";
            String STOP = MINECRAFT + COMMAND + ".stop";
            String TAG = MINECRAFT + COMMAND + ".tag";
            String TELEPORT = MINECRAFT + COMMAND + ".teleport";
            String TELLRAW = MINECRAFT + COMMAND + ".tellraw";
            String TIME = MINECRAFT + COMMAND + ".time";
            String TITLE = MINECRAFT + COMMAND + ".title";
            String TOGGLEDOWNFALL = MINECRAFT + COMMAND + ".toggledownfall";
            String WEATHER = MINECRAFT + COMMAND + ".weather";
            String WHITELIST = MINECRAFT + COMMAND + ".whitelist";
            String WORLDBORDER = MINECRAFT + COMMAND + ".worldborder";
            String XP = MINECRAFT + COMMAND + ".xp";
        }
    }
}
