package me.general_breddok.blockdisplaycreator.common;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Cooldown<K> {
    Map<K, Long> timestamps = new HashMap<>();
    @Getter
    long cooldownValue;
    @Getter
    int timeDivider;

    public Cooldown(long cooldownValue, int timeDivider) {
        if (cooldownValue < 0) {
            cooldownValue = 0;
        }

        if (cooldownValue <= 0) {
            cooldownValue = 1;
        }

        this.cooldownValue = cooldownValue;
        this.timeDivider = timeDivider;
    }

    public Cooldown(long cooldownValue) {
        this(cooldownValue, 1);
    }


    public boolean check(K key) {
        long currentTime = getCurrentTimeUnit();
        Long lastTime = this.timestamps.get(key);

        if (!timestamps.containsKey(key) || currentTime - lastTime > cooldownValue) {
            this.timestamps.put(key, currentTime);
            return true;
        }

        return false;
    }

    public long remaining(K key) {
        Long lastTime = timestamps.get(key);
        if (lastTime == null) return 0L;

        long elapsed = getCurrentTimeUnit() - lastTime;
        return Math.max(0L, cooldownValue - elapsed);
    }

    private long getCurrentTimeUnit() {
        return System.currentTimeMillis() / timeDivider;
    }

    public void reset(K key) {
        timestamps.remove(key);
    }
}
