package org.errig.cycles;

import org.errig.Entities.SystemState;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

@Component
public class CycleManager {

    public void applyCycleProfile(SystemState state, boolean constrain) {
        if (state.isGrowCycle()) {
            // Spectrum and color temp for Grow
            state.setColorFreq(450);
            state.setColorTemp(6500);
            state.setCycleDaysDuration(28);

            if (constrain) {
                enforceRegime(state, 18); // 18h light window
            } else {
                // Derive hours from current on/off times
                state.setCycleHoursDuration(deriveHours(state.getAutoOnTime(), state.getAutoOffTime()));
            }

        } else if (state.isBloomCycle()) {
            // Spectrum and color temp for Bloom
            state.setColorFreq(660);
            state.setColorTemp(3000);
            state.setCycleDaysDuration(91);

            if (constrain) {
                enforceRegime(state, 12); // 12h light window
            } else {
                state.setCycleHoursDuration(deriveHours(state.getAutoOnTime(), state.getAutoOffTime()));
            }
        } else {
            // No cycle selected → ensure hours reflect current window or zero
            if (state.getAutoOnTime() != null && state.getAutoOffTime() != null) {
                state.setCycleHoursDuration(deriveHours(state.getAutoOnTime(), state.getAutoOffTime()));
            } else {
                state.setCycleHoursDuration(0);
            }
        }
    }

    /**
     * Enforce a fixed light window length starting from the current autoOnTime.
     * If autoOnTime is null, default to 06:00.
     */
    private void enforceRegime(SystemState state, int onHours) {
        LocalTime start = state.getAutoOnTime() != null ? state.getAutoOnTime() : LocalTime.of(6, 0);
        LocalTime end = start.plusHours(onHours); // wraps over midnight automatically

        state.setAutoOnTime(start);
        state.setAutoOffTime(end);
        state.setCycleHoursDuration(onHours);
    }

    /**
     * Compute the duration in hours between on and off, handling overnight windows.
     * Example: on=18:00, off=06:00 → 12 hours
     */
    private int deriveHours(LocalTime on, LocalTime off) {
        if (on == null || off == null) return 0;

        Duration d = Duration.between(on, off);
        if (d.isNegative() || d.isZero()) {
            d = d.plusHours(24); // wrap to next day
        }
        // Round down to whole hours; adjust if you need finer granularity
        return (int) d.toHours();
    }
}