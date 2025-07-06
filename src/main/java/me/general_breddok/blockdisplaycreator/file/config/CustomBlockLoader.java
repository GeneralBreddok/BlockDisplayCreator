package me.general_breddok.blockdisplaycreator.file.config;

import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockConfigurationFile;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockRepository;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockYamlFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.nio.file.Path;
import java.util.Map;


@Deprecated
public class CustomBlockLoader {
    private final YamlConfigFile config;
    private final JavaPlugin plugin;
    

    public CustomBlockLoader(YamlConfigFile config, JavaPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    public void moveBlocksToFiles(CustomBlockRepository customBlockRepository) {
        String customBlocksPath = "custom-block";
        Path repository = plugin.getDataFolder().toPath().resolve("custom-blocks");

        ConfigurationSection customBlocksSection = config.getConfigurationSection(customBlocksPath);

        if (customBlocksSection == null) {
            return;
        }

        for (String customBlockName : customBlocksSection.getKeys(false)) {

            if (customBlockRepository.hasFile(customBlockName)) {
                continue;
            }

            ConfigurationSection customBlockSection = customBlocksSection.getConfigurationSection(customBlockName);

            if (customBlockSection == null) {
                continue;
            }

            if (customBlockSection.contains("interaction")) {
                ConfigurationSection section = customBlockSection.getConfigurationSection("interaction");

                if (section.contains("command")) {
                    section.set("right.command", section.get("command"));
                    section.set("command", null);
                }

                if (section.contains("command-source")) {
                    section.set("right.command-source", section.get("command-source"));
                    section.set("command-source", null);
                }


                customBlockSection.set("interactions.interaction", section);
                customBlockSection.set("interaction", null);
            }


            customBlockSection.set("display.translation.x", -0.5);
            customBlockSection.set("display.translation.z", -0.5);

            YamlConfigFile yamlConfigFile = new YamlConfigFile(Path.of(repository.toString(), customBlockName + ".yml"), true);

            yamlConfigFile.fill(customBlockSection);

            customBlockRepository.addFile(new CustomBlockYamlFile(yamlConfigFile));
        }
    }
}
