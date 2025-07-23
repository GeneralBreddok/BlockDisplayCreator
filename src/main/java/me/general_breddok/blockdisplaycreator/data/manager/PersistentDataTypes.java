package me.general_breddok.blockdisplaycreator.data.manager;

import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.MCCommandLine;
import me.general_breddok.blockdisplaycreator.custom.CommandBundle;
import me.general_breddok.blockdisplaycreator.custom.CommandBundleDta;
import me.general_breddok.blockdisplaycreator.custom.block.BDCCustomBlockRotation;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockRotation;
import me.general_breddok.blockdisplaycreator.data.exception.IllegalEnumNameException;
import me.general_breddok.blockdisplaycreator.rotation.DirectedVector;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import me.general_breddok.blockdisplaycreator.sound.ConfigurableSound;
import me.general_breddok.blockdisplaycreator.sound.SimplePlayableSound;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import me.general_breddok.blockdisplaycreator.version.VersionCompat;
import me.general_breddok.blockdisplaycreator.version.VersionManager;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public interface PersistentDataTypes {

    PersistentDataType<String, Material> MATERIAL = PersistentDataTypeStore.newEnumPersistentDataType(Material.class);
    PersistentDataType<String, DyeColor> DYE_COLOR = PersistentDataTypeStore.newEnumPersistentDataType(DyeColor.class);
    PersistentDataType<String, Sound> SOUND = PersistentDataTypeStore.newPersistentDataType(
            String.class,
            Sound.class,
            (complex, context) -> complex.name(),
            (primitive, context) -> {
                try {
                    return Sound.valueOf(primitive);
                } catch (IllegalArgumentException e) {
                    throw new IllegalEnumNameException(primitive + " is not a Sound enum constant", null, primitive);
                }
            }
    );

    PersistentDataType<MemorySection, DirectedVector> DIRECTED_VECTOR = PersistentDataTypeStore.newPersistentDataType(
            MemorySection.class,
            DirectedVector.class,
            (complex, context) -> {
                YamlConfiguration serialized = new YamlConfiguration();
                serialized.set("x", complex.getX());
                serialized.set("y", complex.getY());
                serialized.set("z", complex.getZ());
                serialized.set("yaw", complex.getYaw());
                serialized.set("pitch", complex.getPitch());
                return serialized;
            },
            (primitive, context) -> {
                double x = primitive.getDouble("x", 0);
                double y = primitive.getDouble("y", 0);
                double z = primitive.getDouble("z", 0);
                float yaw = (float) primitive.getDouble("yaw", 0);
                float pitch = (float) primitive.getDouble("pitch", 0);
                return new DirectedVector(x, y, z, yaw, pitch);
            }
    );

    PersistentDataType<ArrayList<?>, List<String>> STRING_LIST = PersistentDataTypeStore.newListPersistentDataType(
            ParameterizedClasses.STRING_LIST,
            (primitive, context) -> {
                ArrayList<String> result = new ArrayList<>(primitive.size());

                for (Object o : primitive) {
                    result.add(String.valueOf(o));
                }

                return result;
            }
    );

    PersistentDataType<String, Class<?>> CLASS = PersistentDataTypeStore.newPersistentDataType(
            String.class,
            ParameterizedClasses.CLASS,
            (complex, context) -> complex.getName(),
            (primitive, context) -> {
                try {
                    return Class.forName(primitive);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
    );
    PersistentDataType<String, CommandBundle.CommandSource> COMMAND_SOURCE = PersistentDataTypeStore.newEnumPersistentDataType(CommandBundle.CommandSource.class);
    PersistentDataType<String, UUID[]> UUID_ARRAY = PersistentDataTypeStore.newPersistentDataType(
            String.class,
            UUID[].class,

            (complex, context) -> {
                if (complex.length == 0) {
                    return "";
                }

                StringBuilder result = new StringBuilder();

                for (UUID uuid : complex) {
                    result.append(uuid).append(",");
                }

                if (!result.isEmpty()) {
                    result.deleteCharAt(result.length() - 1);
                }
                return result.toString();
            },

            (primitive, context) -> {
                if (primitive.isEmpty()) {
                    return new UUID[0];
                }

                String[] parts = primitive.split(",");
                UUID[] result = new UUID[parts.length];

                for (int i = 0; i < parts.length; i++) {
                    if (!parts[i].isEmpty()) {
                        result[i] = java.util.UUID.fromString(parts[i]);
                    }
                }
                return result;
            }
    );

    PersistentDataType<byte[], CommandLine[]> COMMAND_ARRAY = PersistentDataTypeStore.newPersistentDataType(
            byte[].class,
            CommandLine[].class,
            (complex, context) -> {
                String[] strings = Arrays.stream(complex).map(CommandLine::toString).toArray(String[]::new);
                final byte[][] allStringBytes = new byte[strings.length][];
                int total = 0;
                for (int i = 0; i < allStringBytes.length; i++) {
                    final byte[] bytes = strings[i].getBytes(StandardCharsets.UTF_8);
                    allStringBytes[i] = bytes;
                    total += bytes.length;
                }

                final ByteBuffer buffer = ByteBuffer.allocate(total + allStringBytes.length * 4);
                for (final byte[] bytes : allStringBytes) {
                    buffer.putInt(bytes.length);
                    buffer.put(bytes);
                }

                return buffer.array();
            },
            (primitive, context) -> {
                final ByteBuffer buffer = ByteBuffer.wrap(primitive);
                final List<CommandLine> result = new ArrayList<>();

                while (buffer.remaining() > 0) {
                    if (buffer.remaining() < 4) break;
                    final int stringLength = buffer.getInt();
                    if (buffer.remaining() < stringLength) break;

                    final byte[] stringBytes = new byte[stringLength];
                    buffer.get(stringBytes);

                    result.add(new MCCommandLine(new String(stringBytes, StandardCharsets.UTF_8)));
                }

                return result.toArray(CommandLine[]::new);
            }
    );

    PersistentDataType<String, CustomBlockRotation> CUSTOM_BLOCK_ROTATION = PersistentDataTypeStore.newPersistentDataType(
            String.class,
            CustomBlockRotation.class,

            (complex, context) -> complex.getAttachedFace().name() + ":" + complex.getDirection(),

            (primitive, context) -> {
                String[] parts = primitive.split(":");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid serialized string format");
                }

                BlockFace attachedFace = BlockFace.valueOf(parts[0]);
                float direction;
                try {
                    direction = Float.parseFloat(parts[1].trim());
                } catch (NumberFormatException e) {
                    BlockFace directionFace = BlockFace.valueOf(parts[1].toUpperCase());
                    direction = EntityRotation.getYawFromDirectionFace(directionFace);
                }

                return new BDCCustomBlockRotation(attachedFace, direction);
            }
    );

    PersistentDataType<String, UUID> UUID = PersistentDataTypeStore.newPersistentDataType(
            String.class,
            java.util.UUID.class,
            (complex, context) -> complex.toString(),
            (primitive, context) -> java.util.UUID.fromString(primitive)
    );

    PersistentDataType<byte[], Location> LOCATION = PersistentDataTypeStore.newPersistentDataType(
            byte[].class,
            Location.class,

            (complex, context) -> {
                try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); final BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(outputStream)) {
                    bukkitObjectOutputStream.writeObject(complex);
                    return outputStream.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            },

            (primitive, context) -> {
                try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(primitive); final BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(inputStream)) {
                    return (Location) bukkitObjectInputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
    );

    PersistentDataType<String, ItemFlag> ITEM_FLAG = PersistentDataTypeStore.newEnumPersistentDataType(ItemFlag.class);
    PersistentDataType<ArrayList<?>, List<ItemFlag>> ITEM_FLAG_LIST = PersistentDataTypeStore.newListPersistentDataType(
            ParameterizedClasses.ITEM_FLAG_LIST,
            (primitive, context) -> {
                ArrayList<ItemFlag> result = new ArrayList<>(primitive.size());

                for (Object o : primitive) {
                    result.add(
                            ItemFlag.valueOf(
                                    String.valueOf(o)
                            )
                    );
                }

                return result;
            }
    );

    PersistentDataType<String, ItemStack> ITEM = PersistentDataTypeStore.newPersistentDataType(
            String.class,
            ItemStack.class,
            (complex, context) -> ItemUtil.itemToBase64(complex),
            (primitive, context) -> ItemUtil.itemFromBase64(primitive)
    );

    PersistentDataType<MemorySection, ConfigurableSound> PLAYABLE_SOUND = PersistentDataTypeStore.newPersistentDataType(
            MemorySection.class,
            ConfigurableSound.class,
            (complex, context) -> {
                YamlConfiguration serialized = new YamlConfiguration();
                serialized.set("sound-type", complex.getSoundType());
                serialized.set("volume", complex.getVolume());
                serialized.set("pitch", complex.getPitch());
                return serialized;
            },
            (primitive, context) -> {
                String soundTypeName;

                try {
                    soundTypeName = (String) primitive.get("sound-type");
                } catch (ClassCastException | NullPointerException e) {
                    soundTypeName = null;
                }

                if (soundTypeName == null) {
                    return null;
                }

                Sound soundType = SOUND.fromPrimitive(soundTypeName, context);
                Number volume;
                Number pitch;

                try {
                    volume = (Number) primitive.get("volume", 1f);
                } catch (ClassCastException e) {
                    volume = 1f;
                }

                try {
                    pitch = (Number) primitive.get("pitch", 1f);
                } catch (ClassCastException e) {
                    pitch = 1f;
                }

                return new SimplePlayableSound(soundType, volume.floatValue(), pitch.floatValue());
            }

    );

    PersistentDataType<ArrayList<?>, List<CommandLine>> COMMAND_LIST = PersistentDataTypeStore.newListPersistentDataType(
            ParameterizedClasses.COMMAND_LIST,
            (primitive, context) -> {
                ArrayList<CommandLine> result = new ArrayList<>(primitive.size());

                for (Object o : primitive) {
                    result.add(new MCCommandLine(String.valueOf(o)));
                }

                return result;
            }
    );

    PersistentDataType<MemorySection, Display.Brightness> BRIGHTNESS = PersistentDataTypeStore.newPersistentDataType(
            MemorySection.class,
            Display.Brightness.class,
            (complex, context) -> {
                YamlConfiguration serialized = new YamlConfiguration();
                serialized.set("sky", complex.getSkyLight());
                serialized.set("block", complex.getBlockLight());
                return serialized;
            },
            (primitive, context) -> {
                Number sky;
                Number block;

                try {
                    sky = (Number) primitive.get("sky", 10);
                } catch (ClassCastException e) {
                    sky = 10;
                }

                try {
                    block = (Number) primitive.get("block", 10);
                } catch (ClassCastException e) {
                    block = 10;
                }

                return new Display.Brightness(block.intValue(), sky.intValue());
            }
    );

    PersistentDataType<String, Display.Billboard> BILLBOARD = PersistentDataTypeStore.newEnumPersistentDataType(Display.Billboard.class);

    PersistentDataType<MemorySection, Map<Enchantment, Integer>> ENCHANT = PersistentDataTypeStore.newPersistentDataType(
            MemorySection.class,
            ParameterizedClasses.ENCHANT_MAP,
            (complex, context) -> {
                YamlConfiguration serialized = new YamlConfiguration();
                complex.forEach((e, l) -> serialized.set(e.getKey().getKey(), l));
                return serialized;
            },
            (primitive, context) -> {
                Map<Enchantment, Integer> result = new HashMap<>();

                for (String enchantmentName : primitive.getKeys(false)) {

                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));

                    if (enchantment == null) {
                        continue;
                    }

                    Integer level = primitive.getInt(primitive.getCurrentPath() + "." + enchantmentName, 1);

                    result.put(enchantment, level);
                }

                return result;
            }
    );


    PersistentDataType<MemorySection, CommandBundle> COMMAND_BUNDLE = PersistentDataTypeStore.newPersistentDataType(
            MemorySection.class,
            CommandBundle.class,
            (complex, context) -> {
                YamlConfiguration serialized = new YamlConfiguration();

                serialized.set("command", complex.getCommands());
                serialized.set("command-source", complex.getCommandSource());
                serialized.set("granted-command-permission", complex.getGrantedCommandPermissions());

                return serialized;
            },
            (primitive, context) -> {
                List<CommandLine> commands;
                CommandBundle.CommandSource commandSource = CommandBundle.CommandSource.CONSOLE;
                List<String> grantedCommandPermissions = new ArrayList<>();

                ArrayList<?> commandList = (ArrayList<?>) primitive.getList("command");
                ArrayList<?> grantedCommandPermissionList = (ArrayList<?>) primitive.getList("granted-command-permission");
                String commandSourceStr = primitive.getString("command-source");

                if (commandList != null) {
                    commands = COMMAND_LIST.fromPrimitive(commandList, context);
                } else {
                    return null;
                }

                if (grantedCommandPermissionList != null) {
                    grantedCommandPermissions = STRING_LIST.fromPrimitive(grantedCommandPermissionList, context);
                }

                if (commandSourceStr != null) {
                    commandSource = COMMAND_SOURCE.fromPrimitive(commandSourceStr, context);
                }


                return new CommandBundleDta(commands, commandSource, grantedCommandPermissions);
            }
    );

}
