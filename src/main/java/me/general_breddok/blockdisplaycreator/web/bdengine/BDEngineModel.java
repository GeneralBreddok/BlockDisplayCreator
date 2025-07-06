package me.general_breddok.blockdisplaycreator.web.bdengine;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.general_breddok.blockdisplaycreator.commandparser.SummonDisplayCommandLine;
import me.general_breddok.blockdisplaycreator.nbt.entity.NbtEntity;

import java.util.List;

public interface BDEngineModel {
    String getName();

    String getAuthor();

    String getCategory();

    String getImgUrl();

    int getModelCount();

    JsonObject getCommands();

    List<SummonDisplayCommandLine> decodeCommands();

    JsonArray getPassengers();

    List<NbtEntity> decodePassengers();
}
