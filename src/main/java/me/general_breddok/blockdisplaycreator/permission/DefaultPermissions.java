package me.general_breddok.blockdisplaycreator.permission;

public interface DefaultPermissions {
    interface BDC {

        String WG_BYPASS = "bdc.bypass.wg";
        String SKYBLOCK_BYPASS = "bdc.bypass.skyblock";
        String QUICKSELECT_CREATIVE = "bdc.quickselect.creative";

        interface CustomBlock {
            String BLOCKS_DESTRUCTION = "blockdisplaycreator.blocks-destruction";
            String BLOCKS_PLACEMENT = "blockdisplaycreator.blocks-placement";
            String BLOCKS_INTERACTION = "blockdisplaycreator.blocks-interaction";
        }

        interface Command {
            String ERASE_CB_DATA = "bdc.command.erasecbdata";
            String RELOAD = "bdc.command.reload";
            String GIVE_CB = "bdc.command.custom-block.give";
            String BREAK_CB = "bdc.command.custom-block.break";
            String PLACE_CB = "bdc.command.custom-block.place";
            String EDITFILE_CB = "bdc.command.custom-block.editfile";
        }
    }

    interface SuperiorSkyBlock {
        String BYPASS = "superior.skyblock.bypass";
    }

    interface WorldGuard {
        String WORLD_REGION_BYPASS = "worldguard.region.bypass.";
    }

    interface Vanilla {
        interface Command {
            String SUMMON = "minecraft.command.summon";
            String FUNCTION = "minecraft.command.function";
            String PLAYSOUND = "minecraft.command.playsound";
            String BAN = "minecraft.command.ban";
            String BAN_IP = "minecraft.command.ban-ip";
            String BANLIST = "minecraft.command.banlist";
            String DATA = "minecraft.command.data";
            String DATAPACK = "minecraft.command.datapack";
            String DEBUG = "minecraft.command.debug";
            String DIFFICULTY = "minecraft.command.difficulty";
            String CLEAR = "minecraft.command.clear";
            String CLONE = "minecraft.command.clone";
            String DEFAULTGAMEMODE = "minecraft.command.defaultgamemode";
            String DEOP = "minecraft.command.deop";
            String EFFECT = "minecraft.command.effect";
            String ENCHANT = "minecraft.command.enchant";
            String EXECUTE = "minecraft.command.execute";
            String FILL = "minecraft.command.fill";
            String GAMEMODE = "minecraft.command.gamemode";
            String GAMERULE = "minecraft.command.gamerule";
            String GIVE = "minecraft.command.give";
            String HELP = "minecraft.command.help";
            String KICK = "minecraft.command.kick";
            String KILL = "minecraft.command.kill";
            String LIST = "minecraft.command.list";
            String LOCATE = "minecraft.command.locate";
            String ME = "minecraft.command.me";
            String MSG = "minecraft.command.msg";
            String OP = "minecraft.command.op";
            String PARDON = "minecraft.command.pardon";
            String PARDON_IP = "minecraft.command.pardon-ip";
            String PARTICLE = "minecraft.command.particle";
            String RELOAD = "minecraft.command.reload";
            String SAY = "minecraft.command.say";
            String SCOREBOARD = "minecraft.command.scoreboard";
            String SEED = "minecraft.command.seed";
            String SETBLOCK = "minecraft.command.setblock";
            String SETWORLDSPAWN = "minecraft.command.setworldspawn";
            String SPAWNPOINT = "minecraft.command.spawnpoint";
            String SPREADPLAYERS = "minecraft.command.spreadplayers";
            String STOP = "minecraft.command.stop";
            String TAG = "minecraft.command.tag";
            String TELEPORT = "minecraft.command.teleport";
            String TELLRAW = "minecraft.command.tellraw";
            String TIME = "minecraft.command.time";
            String TITLE = "minecraft.command.title";
            String TOGGLEDOWNFALL = "minecraft.command.toggledownfall";
            String WEATHER = "minecraft.command.weather";
            String WHITELIST = "minecraft.command.whitelist";
            String WORLDBORDER = "minecraft.command.worldborder";
            String XP = "minecraft.command.xp";
        }
    }
}
