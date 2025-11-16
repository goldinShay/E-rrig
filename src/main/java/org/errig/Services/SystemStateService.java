package org.errig.Services;

import org.errig.Entities.Actuators.*;
import org.errig.Repositories.CycleLogRepository;
import org.errig.Repositories.SensorLogRepository;
import org.errig.Repositories.SystemStateRepository;
import org.errig.cycles.CycleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SystemStateService {

    @Autowired
    private SystemStateRepository repository;

    @Autowired
    private SensorLogRepository sensorLogRepository;

    @Autowired
    private CycleManager cycleManager;

    @Autowired
    private CycleLogRepository cycleLogRepository;

    public SystemState getLatestState() {
        SystemState state = repository.findTopByOrderByIdDesc();
        SensorLog latestLog = sensorLogRepository.findTopByOrderByTimestampDesc();

        if (state == null) {
            state = initializeDefaultState();
            repository.save(state);
        }

        if (latestLog != null) {
            state.setGeneralPower(true);
            state.setWaterLevelStatus(resolveWaterLevelStatus(latestLog.getWaterLevel()));
            state.setCurrentPowerUse(latestLog.getPowerUse());

            // Apply cycle profile if a cycle is active
            cycleManager.applyCycleProfile(state, true);
        } else {
            state.setGeneralPower(false);
            state.setWaterLevelStatus("Too Low");
            state.setCurrentPowerUse(0.0);
        }

        // ✅ Always re‑evaluate Auto/Manual modes against current time
        applyModes(state);

        // ✅ Persist the updated state so it doesn’t “forget”
        repository.save(state);

        return state;
    }

    private SystemState initializeDefaultState() {
        SystemState state = new SystemState();
        LEDLight defaultLight = new LEDLight(LEDModel.MARS_HYDRO_TSL_2000);
        state.getLedLights().add(defaultLight);

        state.setGrowCycle(false);
        state.setBloomCycle(false);

        state.setLightsMode("Off");
        state.setColorFreq(450);
        state.setColorTemp(6500);
        state.setPumpsMode("Off");
        state.setBlowersMode("Off");
        state.setFansMode("Off");
        state.setFogInducerMode("Off");
        state.setHeaterMode("Off");
        state.setAirVentsMode("Off");

        state.setLightsOn(false);
        state.setPumpsOn(false);
        state.setBlowersOn(false);
        state.setFansOn(false);
        state.setFogInducerOn(false);
        state.setHeaterOn(false);
        state.setAirVentsOn(false);

        // 🌡️ Environmental defaults
        state.setTemperature(20.0);
        state.setEc(1.0);
        state.setPh(7.0);

        // ⏰ Load cycle times from latest CycleLog if available
        CycleLog latestLog = cycleLogRepository.findTopByOrderByUpdatedTsDesc();
        if (latestLog != null) {
            state.setAutoOnTime(latestLog.getPowerOnTime());
            state.setAutoOffTime(latestLog.getPowerOffTime());
            state.setCycleDaysDuration(latestLog.getCycleDurationDays());
            state.setColorFreq(latestLog.getSpectrum());
            state.setTemperature(latestLog.getTemp());
            state.setEc(latestLog.getEc());
            state.setPh(latestLog.getPh());
        } else {
            // Fallback defaults only if no cycle log exists
            state.setAutoOnTime(LocalTime.of(6, 0));
            state.setAutoOffTime(LocalTime.of(18, 0));
            state.setCycleDaysDuration(28);
        }

        return state;
    }

    private String resolveWaterLevelStatus(double level) {
        if (level > 90) return "Too High";
        if (level > 60) return "Optimal";
        if (level > 30) return "Low";
        return "Too Low";
    }

    // 🚀 Start a cycle
    public void startCycle(SystemState state) {
        state.setCycleStartTime(LocalDateTime.now());

        cycleManager.applyCycleProfile(state, true);

        state.setLightsMode("Auto");
        state.setPumpsMode("Auto");
        state.setBlowersMode("Auto");
        state.setFansMode("Auto");
        state.setFogInducerMode("Auto");

        applyModes(state);
        saveState(state);

        // Persist the activation
        logCycleState(state);
    }

    // 🛑 Stop a cycle
    public void stopCycle(SystemState state) {
        state.setGrowCycle(false);
        state.setBloomCycle(false);
        state.setCycleStartTime(null);

        disableAutoModes(state);
        saveState(state);

        // Persist the deactivation
        logCycleState(state);
    }

    public void applyModes(SystemState state) {
        boolean cycleDefined = state.isGrowCycle() || state.isBloomCycle();
        boolean powerOn = Boolean.TRUE.equals(state.isGeneralPower());

        // 🌱 Lights Auto Mode
        if ("Auto".equals(safe(state.getLightsMode())) && cycleDefined && powerOn) {
            LocalTime now = LocalTime.now();
            LocalTime onTime = state.getAutoOnTime();
            LocalTime offTime = state.getAutoOffTime();

            boolean withinWindow = false;
            if (onTime != null && offTime != null) {
                if (onTime.isBefore(offTime) || onTime.equals(offTime)) {
                    // Normal window (e.g. 06:00 → 18:00)
                    withinWindow = !now.isBefore(onTime) && !now.isAfter(offTime);
                } else {
                    // Overnight window (e.g. 18:00 → 06:00 next day)
                    withinWindow = !now.isBefore(onTime) || !now.isAfter(offTime);
                }
            }

            state.setLightsOn(withinWindow);

            for (LEDLight light : state.getLedLights()) {
                if ("Auto".equals(safe(light.getMode()))) {
                    light.setOn(withinWindow);
                    light.setUpdatedTS(LocalDateTime.now());
                }
            }
        } else {
            state.setLightsOn(powerOn && "On".equals(safe(state.getLightsMode())));
        }

        // 🌍 Other devices (no Auto logic yet)
        state.setPumpsOn(powerOn && "On".equals(safe(state.getPumpsMode())));
        state.setBlowersOn(powerOn && "On".equals(safe(state.getBlowersMode())));
        state.setFansOn(powerOn && "On".equals(safe(state.getFansMode())));
        state.setFogInducerOn(powerOn && "On".equals(safe(state.getFogInducerMode())));
        state.setHeaterOn(powerOn && "On".equals(safe(state.getHeaterMode())));
        state.setAirVentsOn(powerOn && "On".equals(safe(state.getAirVentsMode())));

        if (!cycleDefined) {
            disableAutoModes(state);
        }
    }

    private String safe(String mode) {
        return mode == null ? "Off" : mode;
    }

    private void disableAutoModes(SystemState state) {
        if ("Auto".equals(state.getLightsMode())) state.setLightsMode("Off");
        if ("Auto".equals(state.getPumpsMode())) state.setPumpsMode("Off");
        if ("Auto".equals(state.getBlowersMode())) state.setBlowersMode("Off");
        if ("Auto".equals(state.getFansMode())) state.setFansMode("Off");
        if ("Auto".equals(state.getFogInducerMode())) state.setFogInducerMode("Off");
        if ("Auto".equals(state.getHeaterMode())) state.setHeaterMode("Off");
        if ("Auto".equals(state.getAirVentsMode())) state.setAirVentsMode("Off");
    }

    public void simulatePowerUse() {
        SystemState state = getLatestState();
        double simulated = 100 + Math.random() * 400;
        state.setCurrentPowerUse(simulated);
        saveState(state);
    }

    public SystemState saveState(SystemState state) {
        return repository.save(state);
    }

    public SystemState save(SystemState state) {
        return repository.save(state);
    }

    // 🌱 Persist cycle state snapshot
    public void logCycleState(SystemState state) {
        CycleLog log = new CycleLog();
        log.setCycleType(state.isGrowCycle() ? "Grow" : (state.isBloomCycle() ? "Bloom" : "None"));
        log.setUpdatedTs(LocalDateTime.now());
        log.setActive(state.isGrowCycle() || state.isBloomCycle());
        log.setCo2(false);

        log.setPowerOnTime(state.getAutoOnTime());
        log.setPowerOffTime(state.getAutoOffTime());

        log.setCycleDurationDays(state.getCycleDaysDuration() != null ? state.getCycleDaysDuration() : 0);
        log.setSpectrum(state.getColorFreq());

        log.setTemp(state.getTemperature() != null ? state.getTemperature() : 0.0);
        log.setEc(state.getEc() != null ? state.getEc() : 0.0);
        log.setPh(state.getPh() != null ? state.getPh() : 0.0);

        cycleLogRepository.save(log);
    }

    public List<CycleLog> getRecentCycleLogs() {
        return cycleLogRepository.findTop50ByOrderByUpdatedTsDesc();
    }
}
