package org.errig.Services;

import org.errig.Entities.Actuators.LEDLight;
import org.errig.Entities.Sensors.SensorLog;
import org.errig.Entities.SystemState;
import org.errig.Repositories.SensorLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PowerConsumptionManager {

    private final SensorLogRepository sensorLogRepository;

    public PowerConsumptionManager(SensorLogRepository sensorLogRepository) {
        this.sensorLogRepository = sensorLogRepository;
    }

    /**
     * Compute total power consumption (W) based on a given SystemState.
     */
    public double getTotalConsumption(SystemState state) {
        double total = 0.0;

        if (state.getLedLights() != null) {
            for (LEDLight light : state.getLedLights()) {
                if (Boolean.TRUE.equals(light.isOn())) {
                    total += light.getPowerConsumption();
                }
            }
        }

        // TODO: add pumps, fans, etc.
        return total;
    }
}
