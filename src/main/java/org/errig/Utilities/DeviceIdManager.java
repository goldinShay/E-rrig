package org.errig.Utilities;

import org.errig.Entities.DeviceType;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;

public class DeviceIdManager {

    private static final Map<DeviceType, AtomicInteger> counters = new HashMap<>();

    public static String generateId(DeviceType type) {
        counters.putIfAbsent(type, new AtomicInteger(0));
        int id = counters.get(type).incrementAndGet();
        return String.format("%s%02d", type.getIdPrefix(), id);
    }
}
