package me.general_breddok.blockdisplaycreator.commandparser;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommandRegistry {
    private final Map<String, Function<String, CommandLine>> registry = new HashMap<>();

    {
        registry.put("minecraft:summon", SummonCommandLine::new);
        registry.put("minecraft:function", FunctionCommandLine::new);
        registry.put("minecraft:execute", ExecuteCommandLine::new);
    }

    @Nullable
    public CommandLine getCommand(String commandLine, String defaultNameSpace) {
        String name = commandLine.trim().split(" ", 2)[0];

        if (!name.contains(":")) {
            commandLine = defaultNameSpace + ":" + commandLine;
        }


        String finalCommandLine = commandLine;
        return registry.entrySet().stream()
                .filter(entry -> finalCommandLine.startsWith(entry.getKey()))
                .map(entry -> entry.getValue().apply(finalCommandLine))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public CommandLine getCommand(String commandLine) {
        return getCommand(commandLine, "minecraft");
    }

    public void register(String key, Function<String, CommandLine> constructor) {
        registry.put(key, constructor);
    }

    public void unregister(String key) {
        registry.remove(key);
    }
}
