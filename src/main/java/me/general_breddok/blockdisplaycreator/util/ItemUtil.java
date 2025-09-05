package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.function.Predicate;

@UtilityClass
public class ItemUtil {
    public void distributeItem(@NotNull Player player, @NotNull ItemStack item) {
        ItemStack clone = item.clone();
        int amount = item.getAmount();
        while (amount > 0) {
            int stackAmount = Math.min(amount, item.getMaxStackSize());
            clone.setAmount(stackAmount);
            player.getInventory()
                    .addItem(clone)
                    .values()
                    .forEach(itemStack -> dropItemUnderPlayer(player, itemStack));
            amount -= stackAmount;
        }
    }

    private void dropItemUnderPlayer(@NotNull Player player, @NotNull ItemStack item) {
        Item dropped = player.getWorld().dropItemNaturally(player.getLocation(), item);
        dropped.setPickupDelay(10);
    }

    public String itemToBase64(ItemStack item) throws IllegalStateException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public ItemStack itemFromBase64(String data) throws IllegalStateException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            return (ItemStack) dataInput.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalStateException("Unable to decode class type.", e);
        }
    }

    public static boolean selectItem(@NotNull Player player, @NotNull Predicate<ItemStack> filter, @Nullable ItemStack creativeItem, @Nullable String creativeReceivePermission) {
        PlayerInventory inv = player.getInventory();
        int heldItemSlot = inv.getHeldItemSlot();
        ItemStack current = inv.getItem(heldItemSlot);

        if (current != null && filter.test(current)) {
            return true;
        }

        for (int slot = 0; slot < 9; slot++) {
            ItemStack item = inv.getItem(slot);
            if (item != null && filter.test(item)) {
                inv.setHeldItemSlot(slot);
                return true;
            }
        }

        for (int slot = 9; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);
            if (item != null && filter.test(item) && slot < 36) {
                inv.setItem(slot, current);
                inv.setItem(heldItemSlot, item);
                return true;
            }
        }

        if (player.getGameMode() == GameMode.CREATIVE && creativeItem != null) {
            if (creativeReceivePermission != null && !player.hasPermission(creativeReceivePermission)) {
                return false;
            }

            ItemStack heldItem = inv.getItem(heldItemSlot);

            if (heldItem == null || heldItem.getType() == Material.AIR) {
                inv.setItem(heldItemSlot, creativeItem.clone());
                return true;
            }

            for (int slot = 0; slot < 9; slot++) {
                ItemStack item = inv.getItem(slot);
                if (item == null || item.getType() == Material.AIR) {
                    inv.setItem(slot, creativeItem.clone());
                    inv.setHeldItemSlot(slot);
                    return true;
                }
            }

            for (int slot = 9; slot < inv.getSize(); slot++) {
                ItemStack item = inv.getItem(slot);
                if ((item == null || item.getType() == Material.AIR) && slot < 36) {
                    inv.setItem(slot, heldItem);
                    inv.setItem(heldItemSlot, creativeItem.clone());
                    return true;
                }
            }

            inv.setItem(heldItemSlot, creativeItem.clone());
        }

        return false;
    }

    public static boolean selectItem(Player player, Material material) {
        return selectItem(player, item -> item.getType() == material, new ItemStack(material), null);
    }
}
