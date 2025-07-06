package me.general_breddok.blockdisplaycreator.placeholder.universal;

public interface UniversalPlaceholder<T> {
    T context();
    String applyPlaceholders(String template);
}
