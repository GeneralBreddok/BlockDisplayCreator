package me.general_breddok.blockdisplaycreator.data.manager;


import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public interface ParameterizedClasses {
    Class<Class<?>> CLASS = (Class<Class<?>>) (Class<?>) Class.class;
    Class<List<String>> STRING_LIST = (Class<List<String>>) (Class<?>) List.class;
    Class<List<Integer>> INT_LIST = (Class<List<Integer>>) (Class<?>) List.class;
    Class<List<Long>> LONG_LIST = (Class<List<Long>>) (Class<?>) List.class;
    Class<List<Short>> SHORT_LIST = (Class<List<Short>>) (Class<?>) List.class;
    Class<List<Byte>> BYTE_LIST = (Class<List<Byte>>) (Class<?>) List.class;
    Class<List<Boolean>> BOOLEAN_LIST = (Class<List<Boolean>>) (Class<?>) List.class;
    Class<List<Double>> DOUBLE_LIST = (Class<List<Double>>) (Class<?>) List.class;
    Class<List<Float>> FLOAT_LIST = (Class<List<Float>>) (Class<?>) List.class;

    Class<Set<String>> STRING_SET = (Class<Set<String>>) (Class<?>) Set.class;

    Class<Map<String, Integer>> STRING_INTEGER_MAP = (Class<Map<String, Integer>>) (Class<?>) Map.class;
    Class<Map<String, Object>> STRING_OBJECT_MAP = (Class<Map<String, Object>>) (Class<?>) Map.class;
    Class<List<ItemFlag>> ITEM_FLAG_LIST = (Class<List<ItemFlag>>) (Class<?>) List.class;
    Class<List<CommandLine>> COMMAND_LIST = (Class<List<CommandLine>>) (Class<?>) List.class;
    Class<Map<Enchantment, Integer>> ENCHANT_MAP = (Class<Map<Enchantment, Integer>>) (Class<?>) Map.class;
}
