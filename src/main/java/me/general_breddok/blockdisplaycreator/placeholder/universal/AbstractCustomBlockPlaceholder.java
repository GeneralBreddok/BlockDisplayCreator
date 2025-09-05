package me.general_breddok.blockdisplaycreator.placeholder.universal;

import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;

public record AbstractCustomBlockPlaceholder(
        AbstractCustomBlock context) implements UniversalPlaceholder<AbstractCustomBlock> {
    @Override
    public String apply(String template) {
        if (this.context == null) {
            return template;
        }

        if (template.isEmpty()) {
            return template;
        }

        return template
                .replace("%customblock_name%", context.getName());
    }
}
