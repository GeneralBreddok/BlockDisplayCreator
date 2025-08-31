package me.general_breddok.blockdisplaycreator.placeholder.universal;

import me.general_breddok.blockdisplaycreator.util.OperationUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderEngine {
    private final List<UniversalPlaceholder<?>> placeholders;

    public PlaceholderEngine(Collection<UniversalPlaceholder<?>> placeholders) {
        this.placeholders = placeholders.stream().collect(OperationUtil.toArrayList());
    }

    public PlaceholderEngine(UniversalPlaceholder<?>... placeholders) {
        this.placeholders = List.of(placeholders).stream().collect(OperationUtil.toArrayList());
    }

    public PlaceholderEngine() {
        this.placeholders = new ArrayList<>();
    }

    public void add(UniversalPlaceholder<?> placeholder) {
        placeholders.add(placeholder);
    }

    public String apply(String template) {
        for (UniversalPlaceholder<?> placeholder : placeholders) {
            template = placeholder.apply(template);
        }
        return template;
    }
}
