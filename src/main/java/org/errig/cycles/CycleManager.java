package org.errig.cycles;

import org.errig.Entities.SystemState;
import org.springframework.stereotype.Component;

@Component
public class CycleManager {

    public void applyCycleProfile(SystemState state) {
        if (state.isGrowCycle()) {
            state.setColorFreq(450);   // Blue spectrum
            state.setColorTemp(6500);  // Cool daylight

            state.setAutoOnHour(6);
            state.setAutoOnMinute(0);
            state.setAutoOffHour(23);
            state.setAutoOffMinute(59);
            state.setCycleHoursDuration(18);
            state.setCycleDaysDuration(28);

        } else if (state.isBloomCycle()) {
            state.setColorFreq(660);   // Red spectrum
            state.setColorTemp(3000);  // Warm bloom

            state.setAutoOnHour(6);
            state.setAutoOnMinute(0);
            state.setAutoOffHour(17);
            state.setAutoOffMinute(59);
            state.setCycleHoursDuration(12);
            state.setCycleDaysDuration(91);
        }
    }
}
