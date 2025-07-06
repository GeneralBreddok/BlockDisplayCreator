package me.general_breddok.blockdisplaycreator.nbt.entity;

import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;
import me.general_breddok.blockdisplaycreator.nbt.NbtObject;
import me.general_breddok.blockdisplaycreator.nbt.adventure.AdventureTagBuilder;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import net.kyori.adventure.nbt.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;


public class NbtEntityObject extends NbtObject implements NbtEntity {
    public NbtEntityObject(@NotNull CompoundBinaryTag compoundBinaryTag) {
        super(compoundBinaryTag);
    }

    public NbtEntityObject(@NotNull String snbt) throws IOException {
        super(snbt);
    }

    public NbtEntityObject(@NotNull EntityCharacteristics chtc) {
        this(CompoundBinaryTag.empty());
        this.setVelocity(chtc.getVelocity());
        this.setRotation(chtc.getRotation());
        this.setFireTicks(chtc.getFireTicks());
        this.setFreezeTicks(chtc.getFreezeTicks());
        this.setPortalCooldown(chtc.getPortalCooldown());
        this.setVisualFire(chtc.getVisualFire());
        this.setFallDistance(chtc.getFallDistance());
        this.setCustomName(chtc.getCustomName());
        this.setGlowing(chtc.getGlowing());
        this.setCustomNameVisible(chtc.getCustomNameVisible());
        this.setInvulnerable(chtc.getInvulnerable());
        this.setSilent(chtc.getSilent());
        this.setGravity(chtc.getGravity());
        chtc.getPassengers().forEach(entry -> this.addPassenger(entry.getKey(), entry.getValue()));
        chtc.getScoreboardTags().forEach(this::addScoreboardTag);

        if (chtc instanceof NbtEntity nbtEntity) {
            this.setType(nbtEntity.getType());
            this.setUniqueId(nbtEntity.getUniqueId());
            this.setLocation(nbtEntity.getLocation());
            this.setOnGround(nbtEntity.getOnGround());
        }
    }

    @Override
    public Vector getVelocity() {
        double[] motionArray = this.getDoubleArray(NbtEntityPropertyNames.MOTION);

        if (motionArray.length != 3) {
            return null;
        }

        return new Vector(motionArray[0], motionArray[1], motionArray[2]);
    }

    @Override
    public void setVelocity(Vector velocity) {
        if (velocity == null) {
            this.remove(NbtEntityPropertyNames.MOTION);
            return;
        }

        this.put(
                NbtEntityPropertyNames.MOTION,

                AdventureTagBuilder.doubleList(
                        velocity.getX(),
                        velocity.getY(),
                        velocity.getZ()
                ).build()
        );
    }

    @Override
    public EntityRotation getRotation() {
        float[] rotationArray = this.getFloatArray(NbtEntityPropertyNames.ROTATION);
        float yaw = 0;
        float pitch = 0;

        try {
            yaw = rotationArray[0];
        } catch (ArrayIndexOutOfBoundsException ignore) {}

        try {
            pitch = rotationArray[1];
        } catch (ArrayIndexOutOfBoundsException ignore) {}


        return new EntityRotation(yaw, pitch);
    }

    @Override
    public void setRotation(EntityRotation rotation) {
        if (rotation == null) {
            this.remove(NbtEntityPropertyNames.ROTATION);
            return;
        }

        this.put(
                NbtEntityPropertyNames.ROTATION,

                AdventureTagBuilder.floatList(
                        rotation.getYaw(),
                        rotation.getPitch()
                ).build()
        );
    }

    @Override
    public Integer getFireTicks() {
        return (int) this.getShort(NbtEntityPropertyNames.FIRE_TICKS);
    }

    @Override
    public void setFireTicks(Integer ticks) {
        if (ticks == null) {
            this.remove(NbtEntityPropertyNames.FIRE_TICKS);
            return;
        }

        this.put(NbtEntityPropertyNames.FIRE_TICKS, ticks);
    }

    @Override
    public Integer getFreezeTicks() {
        return this.getInt(NbtEntityPropertyNames.FREEZE_TICKS);
    }

    @Override
    public void setFreezeTicks(Integer ticks) {
        if (ticks == null) {
            this.remove(NbtEntityPropertyNames.FREEZE_TICKS);
            return;
        }

        this.put(NbtEntityPropertyNames.FREEZE_TICKS, ticks);
    }

    @Override
    public Integer getPortalCooldown() {
        return this.getInt(NbtEntityPropertyNames.PORTAL_COOLDOWN);
    }

    @Override
    public void setPortalCooldown(Integer portalCooldown) {
        if (portalCooldown == null) {
            this.remove(NbtEntityPropertyNames.PORTAL_COOLDOWN);
            return;
        }

        this.put(NbtEntityPropertyNames.PORTAL_COOLDOWN, portalCooldown);
    }

    @Override
    public Boolean getVisualFire() {
        return this.getBoolean(NbtEntityPropertyNames.VISUAL_FIRE);
    }

    @Override
    public void setVisualFire(Boolean fire) {
        if (fire == null) {
            this.remove(NbtEntityPropertyNames.VISUAL_FIRE);
            return;
        }

        this.put(NbtEntityPropertyNames.VISUAL_FIRE, fire);
    }

    @Override
    public Float getFallDistance() {
        return getFloat(NbtEntityPropertyNames.FALL_DISTANCE);
    }

    @Override
    public void setFallDistance(Float fallDistance) {
        if (fallDistance == null) {
            this.remove(NbtEntityPropertyNames.FALL_DISTANCE);
            return;
        }

        this.put(NbtEntityPropertyNames.FALL_DISTANCE, fallDistance);
    }

    @Override
    public List<Map.Entry<EntityType, EntityCharacteristics>> getPassengers() {
        ListBinaryTag passengersList = this.getList(NbtEntityPropertyNames.PASSENGERS, BinaryTagTypes.COMPOUND);
        List<Map.Entry<EntityType, EntityCharacteristics>> passengers = new ArrayList<>();

        for (BinaryTag binaryTag : passengersList) {
            if (binaryTag instanceof CompoundBinaryTag cbt) {
                NbtEntityObject passenger = new NbtEntityObject(cbt);
                EntityType passengerId = passenger.getType();
                if (passengerId != null) {
                    passengers.add(Map.entry(passengerId, passenger));
                }
            }
        }

        return passengers;
    }

    @Override
    public void setPassengers(List<Map.Entry<EntityType, EntityCharacteristics>> passengers) {
        this.eject();
        ListBinaryTag passengersList = getList(NbtEntityPropertyNames.PASSENGERS);

        for (Map.Entry<EntityType, EntityCharacteristics> passengerChtc : passengers) {
            if (passengerChtc.getValue() instanceof NbtEntity nbtCharacteristics) {
                passengersList = passengersList.add(nbtCharacteristics.asCompound());
            } else {
                NbtEntityObject passenger = new NbtEntityObject(passengerChtc.getValue());
                passenger.setType(passengerChtc.getKey());
                passengersList = passengersList.add(passenger.asCompound());
            }
        }

        this.put(NbtEntityPropertyNames.PASSENGERS, passengersList);
    }

    @Override
    public boolean addPassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics) {
        ListBinaryTag passengersList = this.getList(NbtEntityPropertyNames.PASSENGERS, BinaryTagTypes.COMPOUND);

        if (passengerCharacteristics instanceof NbtEntity nbtCharacteristics) {
            passengersList = passengersList.add(nbtCharacteristics.asCompound());
        } else {
            NbtEntityObject passenger = new NbtEntityObject(passengerCharacteristics);
            passenger.setType(passengerType);
            passengersList = passengersList.add(passenger.asCompound());
        }

        this.put(NbtEntityPropertyNames.PASSENGERS, passengersList);
        return true;
    }

    @Override
    public boolean removePassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics) {
        ListBinaryTag passengersList = this.getList(NbtEntityPropertyNames.PASSENGERS, BinaryTagTypes.COMPOUND);

        NbtEntityObject passengerToRemove = new NbtEntityObject(passengerCharacteristics);
        passengerToRemove.setType(passengerType);

        boolean removed = false;

        for (int i = 0; i < passengersList.size(); i++) {
            if (passengersList.get(i) instanceof CompoundBinaryTag passenger) {
                if (passengerToRemove.equals(passenger)) {
                    passengersList = passengersList.remove(i, null);
                    removed = true;
                    break;
                }
            }
        }

        if (removed) {
            this.put(NbtEntityPropertyNames.PASSENGERS, passengersList);
        }
        return removed;
    }

    @Override
    public void eject() {
        this.remove(NbtEntityPropertyNames.PASSENGERS);
    }

    @Override
    public Boolean getCustomNameVisible() {
        return this.getBoolean(NbtEntityPropertyNames.CUSTOM_NAME_VISIBLE);
    }

    @Override
    public void setCustomNameVisible(Boolean customNameVisible) {
        if (customNameVisible == null) {
            this.remove(NbtEntityPropertyNames.CUSTOM_NAME_VISIBLE);
            return;
        }

        this.put(NbtEntityPropertyNames.CUSTOM_NAME_VISIBLE, customNameVisible);
    }

    @Override
    public Boolean getGlowing() {
        return this.getBoolean(NbtEntityPropertyNames.GLOWING);
    }

    @Override
    public void setGlowing(Boolean glowing) {
        if (glowing == null) {
            this.remove(NbtEntityPropertyNames.GLOWING);
            return;
        }

        this.put(NbtEntityPropertyNames.GLOWING, glowing);
    }

    @Override
    public Boolean getInvulnerable() {
        return this.getBoolean(NbtEntityPropertyNames.INVULNERABLE);
    }

    @Override
    public void setInvulnerable(Boolean invulnerable) {
        if (invulnerable == null) {
            this.remove(NbtEntityPropertyNames.INVULNERABLE);
            return;
        }

        this.put(NbtEntityPropertyNames.INVULNERABLE, invulnerable);
    }

    @Override
    public Boolean getSilent() {
        return this.getBoolean(NbtEntityPropertyNames.SILENT);
    }

    @Override
    public void setSilent(Boolean silent) {
        if (silent == null) {
            this.remove(NbtEntityPropertyNames.SILENT);
            return;
        }

        this.put(NbtEntityPropertyNames.SILENT, silent);
    }

    @Override
    public Boolean getGravity() {
        return this.getBoolean(NbtEntityPropertyNames.GRAVITY);
    }

    @Override
    public void setGravity(Boolean gravity) {
        if (gravity == null) {
            this.remove(NbtEntityPropertyNames.GRAVITY);
            return;
        }

        this.put(NbtEntityPropertyNames.GRAVITY, !gravity);
    }

    @Override
    public Set<String> getScoreboardTags() {
        ListBinaryTag tagsList = this.getList(NbtEntityPropertyNames.TAGS, BinaryTagTypes.STRING);
        Set<String> tags = new HashSet<>();

        for (BinaryTag binaryTag : tagsList) {
            if (binaryTag instanceof StringBinaryTag strTag) {
                tags.add(strTag.value());
            }
        }

        return tags;
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        ListBinaryTag tagsList = this.getList(NbtEntityPropertyNames.TAGS, BinaryTagTypes.STRING);

        if (tag.length() > 64) {
            return false;
        }

        if (tagsList.size() >= 1024) {
            return false;
        }

        tagsList = tagsList.add(StringBinaryTag.stringBinaryTag(tag));
        this.put(NbtEntityPropertyNames.TAGS, tagsList);
        return true;
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        ListBinaryTag tagsList = this.getList(NbtEntityPropertyNames.TAGS, BinaryTagTypes.STRING);
        StringBinaryTag tagToRemove = StringBinaryTag.stringBinaryTag(tag);
        boolean removed = false;

        for (int i = 0; i < tagsList.size(); i++) {
            if (tagsList.get(i) instanceof StringBinaryTag s) {
                if (tagToRemove.value().equals(s.value())) {
                    tagsList = tagsList.remove(i, null);
                    removed = true;
                    break;
                }
            }
        }

        if (removed) {
            this.put(NbtEntityPropertyNames.TAGS, tagsList);
        }
        return removed;
    }

    @Override
    public void clearScoreboardTags() {
        this.remove(NbtEntityPropertyNames.TAGS);
    }

    @Override
    public EntityType getType() {
        EntityType id;
        String idStr = this.getString(NbtEntityPropertyNames.ID);

        if (idStr == null) {
            return null;
        }

        try {
            id = EntityType.valueOf(idStr);
        } catch (IllegalArgumentException e) {
            return null;
        }

        return id;
    }

    @Override
    public void setType(EntityType entityType) {
        if (entityType == null) {
            this.remove(NbtEntityPropertyNames.ID);
            return;
        }

        this.put(NbtEntityPropertyNames.ID, entityType.toString().toLowerCase());
    }

    @Override
    public Location getLocation() {
        double[] posArray = this.getDoubleArray(NbtEntityPropertyNames.POS);

        if (posArray.length < 3) {
            return null;
        }

        return new Location(null, posArray[0], posArray[1], posArray[2]);
    }

    @Override
    public void setLocation(Location location) {
        if (location == null) {
            this.remove(NbtEntityPropertyNames.POS);
            return;
        }

        this.put(
                NbtEntityPropertyNames.POS,

                AdventureTagBuilder.doubleList(
                        location.getX(),
                        location.getY(),
                        location.getZ()
                ).build()
        );
    }

    @Override
    public UUID getUniqueId() {
        int[] uuidArray = this.getIntArray(NbtEntityPropertyNames.UUID);

        if (uuidArray.length < 4) {
            return null;
        }

        long mostSigBits = ((long) uuidArray[0] << 32) | (uuidArray[1] & 0xFFFFFFFFL);
        long leastSigBits = ((long) uuidArray[2] << 32) | (uuidArray[3] & 0xFFFFFFFFL);

        return new UUID(mostSigBits, leastSigBits);
    }

    @Override
    public void setUniqueId(UUID uuid) {
        if (uuid == null) {
            this.remove(NbtEntityPropertyNames.UUID);
            return;
        }

        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        this.put(
                NbtEntityPropertyNames.UUID,

                AdventureTagBuilder.intList(
                        (int) (mostSigBits >> 32),
                        (int) mostSigBits,
                        (int) (leastSigBits >> 32),
                        (int) leastSigBits
                ).build()
        );
    }

    @Override
    public CompoundBinaryTag getData() {
        return this.getCompound(NbtEntityPropertyNames.DATA);
    }

    @Override
    public void setData(CompoundBinaryTag data) {
        if (data == null) {
            this.remove(NbtEntityPropertyNames.DATA);
            return;
        }

        this.put(NbtEntityPropertyNames.DATA, data);
    }

    @Override
    public <T extends NbtEntity> boolean addNbtPassenger(T passenger) {
        ListBinaryTag passengersList = this.getList(NbtEntityPropertyNames.PASSENGERS, BinaryTagTypes.COMPOUND);

        passengersList = passengersList.add(passenger.asCompound());

        put(NbtEntityPropertyNames.PASSENGERS, passengersList);
        return true;
    }

    @Override
    public boolean removeNbtPassenger(int index) {
        ListBinaryTag passengersList = this.getList(NbtEntityPropertyNames.PASSENGERS, BinaryTagTypes.COMPOUND);

        passengersList = passengersList.remove(index, null);

        put(NbtEntityPropertyNames.PASSENGERS, passengersList);
        return true;
    }

    @Override
    public <T extends NbtEntity> boolean removeNbtPassenger(T passsenger) {
        ListBinaryTag passengersList = this.getList(NbtEntityPropertyNames.PASSENGERS, BinaryTagTypes.COMPOUND);

        boolean removed = false;

        for (int i = 0; i < passengersList.size(); i++) {
            if (passengersList.get(i) instanceof CompoundBinaryTag cbt) {
                if (passsenger.equals(cbt)) {
                    passengersList = passengersList.remove(i, null);
                    removed = true;
                    break;
                }
            }
        }

        if (removed) {
            put(NbtEntityPropertyNames.PASSENGERS, passengersList);
        }
        return removed;
    }

    @Override
    public List<NbtEntity> getNbtPassengers() {
        ListBinaryTag passengersList = this.getList(NbtEntityPropertyNames.PASSENGERS, BinaryTagTypes.COMPOUND);
        List<NbtEntity> result = new ArrayList<>();

        for (BinaryTag binary : passengersList) {
            if (binary instanceof CompoundBinaryTag cbt) {
                result.add(new NbtEntityObject(cbt));
            }
        }

        return result;
    }

    @Override
    public void setNbtPassengers(List<? extends NbtEntity> passengers) {
        this.eject();
        ListBinaryTag passengersList = this.getList(NbtEntityPropertyNames.PASSENGERS, BinaryTagTypes.COMPOUND);

        for (NbtEntity passenger : passengers) {
            passengersList = passengersList.add(passenger.asCompound());
        }

        put(NbtEntityPropertyNames.PASSENGERS, passengersList);
    }

    @Override
    public Boolean getOnGround() {
        return this.getBoolean(NbtEntityPropertyNames.ON_GROUND);
    }

    @Override
    public void setOnGround(Boolean onGround) {
        if (onGround == null) {
            this.remove(NbtEntityPropertyNames.ON_GROUND);
            return;
        }

        this.put(NbtEntityPropertyNames.ON_GROUND, onGround);
    }

    @Nullable
    @Override
    public String getCustomName() {
        return this.getString(NbtEntityPropertyNames.CUSTOM_NAME);
    }

    @Override
    public void setCustomName(@Nullable String name) {
        if (name == null) {
            this.remove(NbtEntityPropertyNames.CUSTOM_NAME);
            return;
        }

        this.put(NbtEntityPropertyNames.CUSTOM_NAME, name);
    }
}
