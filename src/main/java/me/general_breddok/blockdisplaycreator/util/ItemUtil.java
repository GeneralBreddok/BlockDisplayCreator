package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

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
}
