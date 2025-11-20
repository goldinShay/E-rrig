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

            // Recommended recipe values
            state.setTemperature(getRecommendedTemp("Grow"));
            state.setEc(getRecommendedEc("Grow"));
            state.setPh(getRecommendedPh("Grow"));

            if (constrain) {
                enforceRegime(state, 18); // 18h light window
            } else {
                state.setCycleHoursDuration(deriveHours(state.getAutoOnTime(), state.getAutoOffTime()));
            }

        } else if (state.isBloomCycle()) {
            // Spectrum and color temp for Bloom
            state.setColorFreq(660);
            state.setColorTemp(3000);
            state.setCycleDaysDuration(91);

            // Recommended recipe values
            state.setTemperature(getRecommendedTemp("Bloom"));
            state.setEc(getRecommendedEc("Bloom"));
            state.setPh(getRecommendedPh("Bloom"));

            if (constrain) {
                enforceRegime(state, 12); // 12h light window
            } else {
                state.setCycleHoursDuration(deriveHours(state.getAutoOnTime(), state.getAutoOffTime()));
            }
        } else {
            if (state.getAutoOnTime() != null && state.getAutoOffTime() != null) {
                state.setCycleHoursDuration(deriveHours(state.getAutoOnTime(), state.getAutoOffTime()));
            } else {
                state.setCycleHoursDuration(0);
            }
        }
    }

    private void enforceRegime(SystemState state, int onHours) {
        LocalTime start = state.getAutoOnTime() != null ? state.getAutoOnTime() : LocalTime.of(6, 0);
        LocalTime end = start.plusHours(onHours);

        state.setAutoOnTime(start);
        state.setAutoOffTime(end);
        state.setCycleHoursDuration(onHours);
    }

    private int deriveHours(LocalTime on, LocalTime off) {
        if (on == null || off == null) return 0;

        Duration d = Duration.between(on, off);
        if (d.isNegative() || d.isZero()) {
            d = d.plusHours(24);
        }
        return (int) d.toHours();
    }

    // 🔧 New recommended values
    public double getRecommendedTemp() {
        return 24.0; // default if no cycle selected
    }

    public double getRecommendedTemp(String cycleType) {
        return "Bloom".equalsIgnoreCase(cycleType) ? 26.0 : 24.0;
    }

    public double getRecommendedEc() {
        return 1.1; // default
    }

    public double getRecommendedEc(String cycleType) {
        return "Bloom".equalsIgnoreCase(cycleType) ? 1.2 : 1.0;
    }

    public double getRecommendedPh() {
        return 6.0; // default
    }

    public double getRecommendedPh(String cycleType) {
        return "Bloom".equalsIgnoreCase(cycleType) ? 6.5 : 6.8;
    }
}
