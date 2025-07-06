package me.general_breddok.blockdisplaycreator.web.player;

import me.general_breddok.blockdisplaycreator.annotation.EmptyCollection;

import java.util.List;

public interface MojangSession {
    String getId();

    String getName();

    @EmptyCollection
    List<MojangSessionProperty> getProperties();

    @EmptyCollection
    List<String> getProfileActions();
}
