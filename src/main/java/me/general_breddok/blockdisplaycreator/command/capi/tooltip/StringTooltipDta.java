package me.general_breddok.blockdisplaycreator.command.capi.tooltip;

import com.mojang.brigadier.Message;
import dev.jorel.commandapi.IStringTooltip;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StringTooltipDta implements IStringTooltip {
    String suggestion;
    Message tooltip;
}
