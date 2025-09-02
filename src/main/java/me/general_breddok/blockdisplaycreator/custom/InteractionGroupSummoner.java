package me.general_breddok.blockdisplaycreator.custom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.entity.RecursiveSummoner;
import me.general_breddok.blockdisplaycreator.entity.Summoner;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Location;
import org.bukkit.block.HangingSign;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InteractionGroupSummoner implements RecursiveSummoner<Interaction>, DeepCloneable<InteractionGroupSummoner> {
    List<Summoner<Interaction>> summoners;

    public InteractionGroupSummoner(List<Summoner<Interaction>> summoners) {
        this.summoners = summoners;
    }

    @SafeVarargs
    public InteractionGroupSummoner(Summoner<Interaction>... summoners) {
        this.summoners = Arrays.stream(summoners).collect(OperationUtil.toArrayList());
    }

    @Override
    public List<Interaction> summon(@NotNull Location location, @Nullable Predicate<Interaction> filter) {
        if (summoners.isEmpty()) {
            return Collections.emptyList();
        }

        return summoners.stream()
                .map(summoner -> summoner.summon(location))
                .filter(interaction -> filter == null || filter.test(interaction))
                .collect(OperationUtil.toArrayList());
    }

    @Override
    public InteractionGroupSummoner clone() {
        return new InteractionGroupSummoner(DeepCloneable.tryCloneList(this.summoners));
    }
}
