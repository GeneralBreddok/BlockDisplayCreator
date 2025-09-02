package me.general_breddok.blockdisplaycreator.data.manager;

import com.google.common.reflect.TypeToken;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.custom.CommandBundle;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockPlacementMode;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockRotation;
import me.general_breddok.blockdisplaycreator.rotation.DirectedVector;
import me.general_breddok.blockdisplaycreator.sound.PlayableSound;
import org.bukkit.*;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;

public interface TypeTokens {
    TypeToken<Boolean> BOOLEAN = TypeToken.of(Boolean.class);
    TypeToken<Byte> BYTE = TypeToken.of(Byte.class);
    TypeToken<Short> SHORT = TypeToken.of(Short.class);
    TypeToken<Integer> INTEGER = TypeToken.of(Integer.class);
    TypeToken<Long> LONG = TypeToken.of(Long.class);
    TypeToken<Float> FLOAT = TypeToken.of(Float.class);
    TypeToken<Double> DOUBLE = TypeToken.of(Double.class);
    TypeToken<Character> CHARACTER = TypeToken.of(Character.class);
    TypeToken<String> STRING = TypeToken.of(String.class);


    TypeToken<boolean[]> BOOLEAN_ARRAY = new TypeToken<>() {};
    TypeToken<byte[]> BYTE_ARRAY = new TypeToken<>() {};
    TypeToken<short[]> SHORT_ARRAY = new TypeToken<>() {};
    TypeToken<int[]> INTEGER_ARRAY = new TypeToken<>() {};
    TypeToken<long[]> LONG_ARRAY = new TypeToken<>() {};
    TypeToken<float[]> FLOAT_ARRAY = new TypeToken<>() {};
    TypeToken<double[]> DOUBLE_ARRAY = new TypeToken<>() {};
    TypeToken<char[]> CHARACTER_ARRAY = new TypeToken<>() {};
    TypeToken<String[]> STRING_ARRAY = new TypeToken<>() {};
    TypeToken<UUID[]> UUID_ARRAY = new TypeToken<>() {};


    TypeToken<List<Boolean>> BOOLEAN_LIST = new TypeToken<>() {};
    TypeToken<List<Byte>> BYTE_LIST = new TypeToken<>() {};
    TypeToken<List<Short>> SHORT_LIST = new TypeToken<>() {};
    TypeToken<List<Integer>> INTEGER_LIST = new TypeToken<>() {};
    TypeToken<List<Long>> LONG_LIST = new TypeToken<>() {};
    TypeToken<List<Float>> FLOAT_LIST = new TypeToken<>() {};
    TypeToken<List<Double>> DOUBLE_LIST = new TypeToken<>() {};
    TypeToken<List<Character>> CHARACTER_LIST = new TypeToken<>() {};
    TypeToken<List<String>> STRING_LIST = new TypeToken<>() {};
    TypeToken<List> LIST = new TypeToken<>() {};
    TypeToken<ArrayList> ARRAY_LIST = new TypeToken<>() {};


    TypeToken<Set<String>> STRING_SET = new TypeToken<>() {};

    TypeToken<Map<String, Object>> STRING_OBJECT_MAP = new TypeToken<>() {};
    TypeToken<Map<Integer, Object>> INTEGER_OBJECT_MAP = new TypeToken<>() {};

    TypeToken<Material> MATERIAL = TypeToken.of(Material.class);
    TypeToken<CustomBlockPlacementMode> CUSTOM_BLOCK_PLACEMENT_MODE = TypeToken.of(CustomBlockPlacementMode.class);
    TypeToken<Location> LOCATION = TypeToken.of(Location.class);
    TypeToken<ItemFlag> ITEM_FLAG = TypeToken.of(ItemFlag.class);
    TypeToken<Sound> SOUND = TypeToken.of(Sound.class);
    TypeToken<CommandBundle.CommandSource> COMMAND_SOURCE = TypeToken.of(CommandBundle.CommandSource.class);
    TypeToken<PlayableSound> PLAYABLE_SOUND = TypeToken.of(PlayableSound.class);
    TypeToken<java.util.UUID> UUID = TypeToken.of(UUID.class);
    TypeToken<PersistentDataContainer> PERSISTENT_DATA_CONTAINER = TypeToken.of(PersistentDataContainer.class);
    TypeToken<PersistentDataContainer[]> PERSISTENT_DATA_CONTAINER_ARRAY = TypeToken.of(PersistentDataContainer[].class);
    TypeToken<CustomBlockRotation> CUSTOM_BLOCK_ROTATION = TypeToken.of(CustomBlockRotation.class);
    TypeToken<Display.Billboard> BILLBOARD = TypeToken.of(Display.Billboard.class);
    TypeToken<Display.Brightness> BRIGHTNESS = TypeToken.of(Display.Brightness.class);
    TypeToken<Vector> VECTOR = TypeToken.of(Vector.class);
    TypeToken<DirectedVector> DIRECTED_VECTOR = TypeToken.of(DirectedVector.class);
    TypeToken<BlockVector> BLOCK_VECTOR = TypeToken.of(BlockVector.class);
    TypeToken<ItemStack> ITEM_STACK = TypeToken.of(ItemStack.class);
    TypeToken<Color> COLOR = TypeToken.of(Color.class);
    TypeToken<AttributeModifier> ATTRIBUTE_MODIFIER = TypeToken.of(AttributeModifier.class);
    TypeToken<BoundingBox> BOUNDING_BOX = TypeToken.of(BoundingBox.class);
    TypeToken<FireworkEffect> FIREWORK_EFFECT = TypeToken.of(FireworkEffect.class);

    TypeToken<List<ItemFlag>> ITEM_FLAG_LIST = new TypeToken<>() {};
    TypeToken<List<CommandLine>> COMMAND_LIST = new TypeToken<>() {};
    TypeToken<Map<Enchantment, Integer>> ENCHANT_MAP = new TypeToken<>() {};

    TypeToken<Class<?>> CLASS = new TypeToken<>() {};
    TypeToken<CommandBundle> COMMAND_BUNDLE = TypeToken.of(CommandBundle.class);
    TypeToken<CommandLine[]> COMMAND_ARRAY = TypeToken.of(CommandLine[].class);
    TypeToken<DyeColor> DYE_COLOR = TypeToken.of(DyeColor.class);
}
