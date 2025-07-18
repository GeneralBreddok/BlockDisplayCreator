package me.general_breddok.blockdisplaycreator.command.capi.tooltip;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractCustomBlockTooltip/* implements IStringTooltip*/ {
    /*@Getter
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
    }*/
}
