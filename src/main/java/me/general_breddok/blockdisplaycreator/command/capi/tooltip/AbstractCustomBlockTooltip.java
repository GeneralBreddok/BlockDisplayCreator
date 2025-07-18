package me.general_breddok.blockdisplaycreator.command.tooltip;

import com.mojang.brigadier.Message;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.Tooltip;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import org.bukkit.inventory.ItemStack;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractCustomBlockTooltip implements IStringTooltip {
    @Getter
    AbstractCustomBlock abstractCustomBlock;
    final ItemStack item;

    public AbstractCustomBlockTooltip(AbstractCustomBlock abstractCustomBlock) {
        this.abstractCustomBlock = abstractCustomBlock;
        this.item = abstractCustomBlock.getItem();
    }

    @Override
    public String getSuggestion() {
        return abstractCustomBlock.getName();
    }

    @Override
    public Message getTooltip() {
        return Tooltip.messageFromString(item.getItemMeta().getDisplayName());
    }
}
