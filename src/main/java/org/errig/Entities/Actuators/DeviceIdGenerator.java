package org.errig.Entities.Actuators;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;

public class DeviceIdGenerator {
    private static final Map<DeviceType, AtomicInteger> counters = new HashMap<>();

    static {
        for (DeviceType type : DeviceType.values()) {
            counters.put(type, new AtomicInteger(1));
        }
    }

    public static String generate(DeviceType type) {
        int count = counters.get(type).getAndIncrement();
        return String.format("%s%02d", type.getIdPrefix(), count);
    }
}