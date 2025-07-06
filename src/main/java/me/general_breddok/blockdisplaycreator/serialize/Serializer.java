package me.general_breddok.blockdisplaycreator.serialize;

public interface Serializer<F, T> {
    T serialize(F obj);
}
