package me.general_breddok.blockdisplaycreator.data.yaml;

import lombok.Getter;
import lombok.NonNull;
import me.general_breddok.blockdisplaycreator.common.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class ConfigPathBuilder implements Builder<String> {
    private final List<String> pathSections;
    private String fullPath;

    public ConfigPathBuilder(@NonNull String path) {
        this(Arrays.asList(path.split("\\.")));
    }

    public ConfigPathBuilder(@NonNull List<String> pathSections) {
        this(pathSections.toArray(String[]::new));
    }

    public ConfigPathBuilder(@NonNull String... pathSections) {
        validateSections(pathSections);
        this.pathSections = new ArrayList<>(List.of(pathSections));
        updateFullPath();
    }

    public ConfigPathBuilder(@NonNull Set<String> pathSections) {
        this(pathSections.toArray(String[]::new));
    }

    private void validateSections(@NonNull String[] sections) {
        if (sections.length == 0) {
            throw new IllegalArgumentException("Section list cannot be null or empty.");
        }
        for (String section : sections) {
            if (section.trim().isEmpty()) {
                throw new IllegalArgumentException("Section cannot be null or empty.");
            }
        }
    }

    public void replaceSection(int index, @NotNull String newSection) {
        if (newSection.trim().isEmpty()) {
            throw new IllegalArgumentException("New section cannot be empty.");
        }
        pathSections.set(index, newSection);
        updateFullPath();
    }

    public ConfigPathBuilder addSection(int index, @NotNull String section) {
        if (section.trim().isEmpty()) {
            throw new IllegalArgumentException("Section cannot be empty.");
        }
        pathSections.add(index, section);
        updateFullPath();
        return this;
    }


    private void updateFullPath() {
        this.fullPath = String.join(".", pathSections);
    }

    public String getSection(int index) {
        if (index < 0 || index >= pathSections.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        return pathSections.get(index);
    }

    public String getLastSection() {
        if (pathSections.isEmpty()) {
            throw new IllegalStateException("Path is empty.");
        }
        return pathSections.get(pathSections.size() - 1);
    }

    public int getSectionCount() {
        return pathSections.size();
    }

    public boolean hasSection(String section) {
        return pathSections.contains(section);
    }

    public boolean isSubPath(ConfigPathBuilder other) {
        return new HashSet<>(pathSections).containsAll(other.getPathSections());
    }

    public ConfigPathBuilder appendSection(@NonNull String section) {
        if (section.trim().isEmpty()) {
            throw new IllegalArgumentException("Section cannot be empty.");
        }
        List<String> newSections = new ArrayList<>(pathSections);
        newSections.add(section);
        return new ConfigPathBuilder(newSections);
    }

    public ConfigPathBuilder parentPath() {
        if (pathSections.size() <= 1) {
            throw new IllegalStateException("Path does not have a parent.");
        }
        return new ConfigPathBuilder(pathSections.subList(0, pathSections.size() - 1));
    }

    public String getFirstSection() {
        if (pathSections.isEmpty()) {
            throw new IllegalStateException("Path is empty.");
        }
        return pathSections.get(0);
    }

    public void removeSection(int index) {
        if (index < 0 || index >= pathSections.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        pathSections.remove(index);
        updateFullPath();
    }

    public boolean isRoot() {
        return pathSections.size() == 1;
    }

    @Override
    public ConfigPathBuilder clone() {
        return new ConfigPathBuilder(pathSections);
    }

    @Override
    public String toString() {
        return getFullPath();
    }

    @Override
    public String build() {
        return getFullPath();
    }
}
