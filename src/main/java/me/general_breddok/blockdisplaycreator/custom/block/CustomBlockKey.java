package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface CustomBlockKey {
    NamespacedKey SERVICE_CLASS = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-service-class");
    NamespacedKey NAME = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-material");

    NamespacedKey LOCATION = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-location");
    NamespacedKey CUSTOM_BLOCK_UUID = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-uuid");
    NamespacedKey DISPLAY_UUID = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-display-uuid");
    NamespacedKey INTERACTION_UUID = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-interaction-uuid");
    NamespacedKey COLLISION_UUID = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-collision-uuid");
    NamespacedKey BLOCK_ROTATION = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-rotation");

    NamespacedKey INTERACTION_IDENTIFIER = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-interaction-identifier");
    NamespacedKey COLLISION_IDENTIFIER = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-collision-identifier");


    NamespacedKey DISPLAY_SPAWN_COMMAND = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-display-spawn-command");
    NamespacedKey ITEM = new NamespacedKey(BlockDisplayCreator.getInstance(), "custom-block-item");

    static DataHolder holder(PersistentDataHolder holder) {
        return new DataHolder(holder);
    }

    class DataHolder {
        private final PersistentDataHolder holder;

        private DataHolder(PersistentDataHolder holder) {
            this.holder = holder;
        }

        @Nullable
        public String getServiceClass() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            return persistentData.get(SERVICE_CLASS);
        }

        public DataHolder setServiceClass(@NotNull String serviceClass) {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            persistentData.set(SERVICE_CLASS, serviceClass);
            return this;
        }

        @Nullable
        public String getName() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            return persistentData.get(NAME);
        }

        public DataHolder setName(@NotNull String name) {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            persistentData.set(NAME, name);
            return this;
        }

        @Nullable
        public Location getLocation() {
            PersistentData<Location> persistentData = new PersistentData<>(holder, TypeTokens.LOCATION);

            return persistentData.get(LOCATION);
        }

        public DataHolder setLocation(@NotNull Location location) {
            PersistentData<Location> persistentData = new PersistentData<>(holder, TypeTokens.LOCATION);

            persistentData.set(LOCATION, location);
            return this;
        }

        @Nullable
        public CustomBlockRotation getRotation() {
            PersistentData<CustomBlockRotation> persistentData = new PersistentData<>(holder, TypeTokens.CUSTOM_BLOCK_ROTATION);

            return persistentData.get(BLOCK_ROTATION);
        }

        public DataHolder setRotation(@NotNull CustomBlockRotation rotation) {
            PersistentData<CustomBlockRotation> persistentData = new PersistentData<>(holder, TypeTokens.CUSTOM_BLOCK_ROTATION);

            persistentData.set(BLOCK_ROTATION, rotation);
            return this;
        }

        @Nullable
        public UUID[] getDisplayUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            return persistentData.get(DISPLAY_UUID);
        }

        public DataHolder setDisplayUUID(@NotNull UUID[] vehiclesUUID) {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            persistentData.set(DISPLAY_UUID, vehiclesUUID);
            return this;
        }

        @Nullable
        public UUID[] getInteractionUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            return persistentData.get(INTERACTION_UUID);
        }

        public DataHolder setInteractionUUID(@NotNull UUID[] interactionUUID) {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            persistentData.set(INTERACTION_UUID, interactionUUID);
            return this;
        }

        @Nullable
        public UUID[] getCollisionUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            return persistentData.get(COLLISION_UUID);
        }

        public DataHolder setCollisionUUID(@NotNull UUID[] collisionUUID) {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            persistentData.set(COLLISION_UUID, collisionUUID);
            return this;
        }

        @Nullable
        public UUID getCustomBlockUUID() {
            PersistentData<UUID> persistentData = new PersistentData<>(holder, TypeTokens.UUID);

            return persistentData.get(CUSTOM_BLOCK_UUID);
        }

        public DataHolder setCustomBlockUUID(@NotNull UUID uuid) {
            PersistentData<UUID> persistentData = new PersistentData<>(holder, TypeTokens.UUID);

            persistentData.set(CUSTOM_BLOCK_UUID, uuid);
            return this;
        }

        @Nullable
        public String getInteractionIdentifier() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            return persistentData.get(INTERACTION_IDENTIFIER);
        }

        public DataHolder setInteractionIdentifier(@NotNull String identifier) {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            persistentData.set(INTERACTION_IDENTIFIER, identifier);
            return this;
        }

        @Nullable
        public String getCollisionIdentifier() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            return persistentData.get(COLLISION_IDENTIFIER);
        }

        public DataHolder setCollisionIdentifier(@NotNull String identifier) {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            persistentData.set(COLLISION_IDENTIFIER, identifier);
            return this;
        }

        public DataHolder removeServiceClass() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            persistentData.remove(SERVICE_CLASS);
            return this;
        }

        public DataHolder removeName() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            persistentData.remove(NAME);
            return this;
        }

        public DataHolder removeLocation() {
            PersistentData<Location> persistentData = new PersistentData<>(holder, TypeTokens.LOCATION);

            persistentData.remove(LOCATION);
            return this;
        }

        public DataHolder removeRotation() {
            PersistentData<CustomBlockRotation> persistentData = new PersistentData<>(holder, TypeTokens.CUSTOM_BLOCK_ROTATION);

            persistentData.remove(BLOCK_ROTATION);
            return this;
        }

        public DataHolder removeDisplayUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            persistentData.remove(DISPLAY_UUID);
            return this;
        }

        public DataHolder removeInteractionUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            persistentData.remove(INTERACTION_UUID);
            return this;
        }

        public DataHolder removeCollisionUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            persistentData.remove(COLLISION_UUID);
            return this;
        }

        public DataHolder removeCustomBlockUUID() {
            PersistentData<UUID> persistentData = new PersistentData<>(holder, TypeTokens.UUID);

            persistentData.remove(CUSTOM_BLOCK_UUID);
            return this;
        }

        public DataHolder removeInteractionIdentifier() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            persistentData.remove(INTERACTION_IDENTIFIER);
            return this;
        }

        public DataHolder removeCollisionIdentifier() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            persistentData.remove(COLLISION_IDENTIFIER);
            return this;
        }


        public boolean hasServiceClass() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            return persistentData.has(SERVICE_CLASS);
        }

        public boolean hasName() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            return persistentData.has(NAME);
        }

        public boolean hasLocation() {
            PersistentData<Location> persistentData = new PersistentData<>(holder, TypeTokens.LOCATION);

            return persistentData.has(LOCATION);
        }

        public boolean hasRotation() {
            PersistentData<CustomBlockRotation> persistentData = new PersistentData<>(holder, TypeTokens.CUSTOM_BLOCK_ROTATION);

            return persistentData.has(BLOCK_ROTATION);
        }

        public boolean hasDisplayUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            return persistentData.has(DISPLAY_UUID);
        }

        public boolean hasInteractionUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            return persistentData.has(INTERACTION_UUID);
        }

        public boolean hasCollisionUUID() {
            PersistentData<UUID[]> persistentData = new PersistentData<>(holder, TypeTokens.UUID_ARRAY);

            return persistentData.has(COLLISION_UUID);
        }

        public boolean hasCustomBlockUUID() {
            PersistentData<UUID> persistentData = new PersistentData<>(holder, TypeTokens.UUID);

            return persistentData.has(CUSTOM_BLOCK_UUID);
        }

        public boolean hasInteractionIdentifier() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            return persistentData.has(INTERACTION_IDENTIFIER);
        }

        public boolean hasCollisionIdentifier() {
            PersistentData<String> persistentData = new PersistentData<>(holder, TypeTokens.STRING);

            return persistentData.has(COLLISION_IDENTIFIER);
        }
    }
}
