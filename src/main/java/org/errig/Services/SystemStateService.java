package org.errig.Services;

import org.errig.Entities.Actuators.*;
import org.errig.Entities.Sensors.SensorLog;
import org.errig.Entities.SystemState;
import org.errig.Repositories.CycleLogRepository;
import org.errig.Repositories.SensorLogRepository;
import org.errig.Repositories.SystemStateRepository;
import org.errig.cycles.CycleManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SystemStateService {

    private final SystemStateRepository repository;
    private final SensorLogRepository sensorLogRepository;
    private final CycleManager cycleManager;
    private final CycleLogRepository cycleLogRepository;
    private final PowerConsumptionManager powerConsumptionManager;
    private final WeatherService weatherService;

    public SystemStateService(SystemStateRepository repository,
                              SensorLogRepository sensorLogRepository,
                              CycleManager cycleManager,
                              CycleLogRepository cycleLogRepository,
                              PowerConsumptionManager powerConsumptionManager,
                              WeatherService weatherService) {
        this.repository = repository;
        this.sensorLogRepository = sensorLogRepository;
        this.cycleManager = cycleManager;
        this.cycleLogRepository = cycleLogRepository;
        this.powerConsumptionManager = powerConsumptionManager;
        this.weatherService = weatherService;
    }

    public SystemState getLatestState() {
        SystemState state = repository.findTopByOrderByIdDesc().orElse(null);
        SensorLog latestLog = sensorLogRepository.findTopByOrderByTimestampDesc();

        if (state == null) {
            state = initializeDefaultState();
            repository.save(state);
        }

        if (latestLog != null) {
            state.setGeneralPower(true);
            state.setWaterLevelStatus(resolveWaterLevelStatus(latestLog.getWaterLevel()));

            double total = powerConsumptionManager.getTotalConsumption(state);
            state.setCurrentPowerUse(total);

            cycleManager.applyCycleProfile(state, true);
        } else {
            state.setGeneralPower(false);
            state.setWaterLevelStatus("Too Low");
            state.setCurrentPowerUse(0.0);
        }

        // 🌱 Hydrate cycle info from latest CycleLog
        CycleLog activeCycle = cycleLogRepository.findTopByOrderByUpdatedTsDesc();
        if (activeCycle != null && activeCycle.isActive()) {
            state.setGrowCycle("Grow".equalsIgnoreCase(activeCycle.getCycleType()));
            state.setBloomCycle("Bloom".equalsIgnoreCase(activeCycle.getCycleType()));
            state.setCycleStartTime(activeCycle.getUpdatedTs());
            state.setAutoOnTime(activeCycle.getPowerOnTime());
            state.setAutoOffTime(activeCycle.getPowerOffTime());
            state.setCycleDaysDuration(activeCycle.getCycleDurationDays());
            state.setColorFreq(activeCycle.getSpectrum());
            state.setTemperature(activeCycle.getTemp());
            state.setEc(activeCycle.getEc());
            state.setPh(activeCycle.getPh());
        }

        // 🌤️ External air temp from weather API
        double externalTemp = weatherService.fetchExternalAirTemp();
        state.setExternalAirTemp(externalTemp);

        // 🌡️ Internal air temp simulated until sensor hardware is ready
        state.setAirTemp(20 + Math.random() * 5);

        applyModes(state);
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

        state.setTemperature(20.0);
        state.setEc(1.0);
        state.setPh(7.0);

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
        logCycleState(state);
    }

    // 🛑 Stop a cycle
    public void stopCycle(SystemState state) {
        state.setGrowCycle(false);
        state.setBloomCycle(false);
        state.setCycleStartTime(null);

        disableAutoModes(state);
        saveState(state);
        logCycleState(state);
    }

    public void applyModes(SystemState state) {
        boolean cycleDefined = state.isGrowCycle() || state.isBloomCycle();
        boolean powerOn = Boolean.TRUE.equals(state.isGeneralPower());

        if ("Auto".equals(safe(state.getLightsMode())) && cycleDefined && powerOn) {
            LocalTime now = LocalTime.now();
            LocalTime onTime = state.getAutoOnTime();
            LocalTime offTime = state.getAutoOffTime();

            boolean withinWindow = false;
            if (onTime != null && offTime != null) {
                if (onTime.isBefore(offTime) || onTime.equals(offTime)) {
                    withinWindow = !now.isBefore(onTime) && !now.isAfter(offTime);
                } else {
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
        double total = powerConsumptionManager.getTotalConsumption(state);
        state.setCurrentPowerUse(total);
        saveState(state);
    }

    public SystemState saveState(SystemState state) {
        return repository.save(state);
    }

    public SystemState save(SystemState state) {
        return repository.save(state);
    }

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

        // ✅ Use recommended values from the cycle profile, not live state
        log.setTemp(cycleManager.getRecommendedTemp());
        log.setEc(cycleManager.getRecommendedEc());
        log.setPh(cycleManager.getRecommendedPh());

        cycleLogRepository.save(log);
    }

    public List<CycleLog> getRecentCycleLogs() {
        return cycleLogRepository.findTop50ByOrderByUpdatedTsDesc();
    }

    public SystemState getCurrentState() {
        return repository.findTopByOrderByUpdatedTsDesc()
                .orElseGet(SystemState::new);
        // fallback: empty state if none exists
    }
}
