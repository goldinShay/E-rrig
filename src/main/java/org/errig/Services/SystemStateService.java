package org.errig.Services;

import org.errig.Entities.LEDLight;
import org.errig.Entities.LEDModel;
import org.errig.Entities.SensorLog;
import org.errig.Entities.SystemState;
import org.errig.Repositories.SensorLogRepository;
import org.errig.Repositories.SystemStateRepository;
import org.errig.cycles.CycleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class SystemStateService {

    @Autowired
    private SystemStateRepository repository;

    @Autowired
    private SensorLogRepository sensorLogRepository;
    @Autowired
    private CycleManager cycleManager;

    public SystemState getLatestState() {
        SystemState state = repository.findTopByOrderByIdDesc();
        SensorLog latestLog = sensorLogRepository.findTopByOrderByTimestampDesc();

        if (state == null) {
            state = new SystemState();
            LEDLight defaultLight = new LEDLight(LEDModel.MARS_HYDRO_TSL_2000);
            state.getLedLights().add(defaultLight);

            // No cycle selected yet
            state.setGrowCycle(false);
            state.setBloomCycle(false);

            // Default all actuator modes to "Off"
            state.setLightsMode("Off");
            state.setColorFreq(450); // default to blue spectrum
            state.setColorTemp(6500); // default to cool daylight
            state.setPumpsMode("Off");
            state.setBlowersMode("Off");
            state.setFansMode("Off");
            state.setFogInducerMode("Off");
            state.setHeaterMode("Off");
            state.setAirVentsMode("Off");

            // Default all actuator states to false
            state.setLightsOn(false);
            state.setPumpsOn(false);
            state.setBlowersOn(false);
            state.setFansOn(false);
            state.setFogInducerOn(false);
            state.setHeaterOn(false);
            state.setAirVentsOn(false);

            repository.save(state);
        }

        // Sync with latest sensor log
        if (latestLog != null) {
            state.setGeneralPower(true); // System is alive
            state.setWaterLevelStatus(resolveWaterLevelStatus(latestLog.getWaterLevel()));
            state.setCurrentPowerUse(latestLog.getPowerUse());

            // 🌱 Log current cycle status
            if (state.isGrowCycle()) {
                System.out.println("Cycle: Grow");
            } else if (state.isBloomCycle()) {
                System.out.println("Cycle: Bloom");
            } else {
                System.out.println("Cycle: None");
            }

            // 🌈 Apply light spectrum and temperature based on cycle
            cycleManager.applyCycleProfile(state);
        } else {
            state.setGeneralPower(false); // No heartbeat
            state.setWaterLevelStatus("Too Low");
            state.setCurrentPowerUse(0.0);
        }

        return state;
    }

    private String resolveWaterLevelStatus(double level) {
        if (level > 90) return "Too High";
        if (level > 60) return "Optimal";
        if (level > 30) return "Low";
        return "Too Low";
    }

    public SystemState saveState(SystemState state) {
        return repository.save(state);
    }

    public void simulatePowerUse() {
        SystemState state = getLatestState();
        double simulated = 100 + Math.random() * 400;
        state.setCurrentPowerUse(simulated);
        saveState(state);
    }

    public void startCycle(SystemState state) {
        LocalDateTime now = LocalDateTime.now();
        state.setCycleStartTime(now);

        if (state.isGrowCycle()) {
            // Set Grow cycle schedule
            state.setAutoOnHour(6);
            state.setAutoOnMinute(0);
            state.setAutoOffHour(23);
            state.setAutoOffMinute(59);
            state.setCycleHoursDuration(18);
            state.setCycleDaysDuration(28);
        } else if (state.isBloomCycle()) {
            // Set Bloom cycle schedule
            state.setAutoOnHour(6);
            state.setAutoOnMinute(0);
            state.setAutoOffHour(17);
            state.setAutoOffMinute(59);
            state.setCycleHoursDuration(12);
            state.setCycleDaysDuration(91);
        }

        // Set all relevant actuators to Auto mode
        state.setLightsMode("Auto");
        state.setPumpsMode("Auto");
        state.setBlowersMode("Auto");
        state.setFansMode("Auto");
        state.setFogInducerMode("Auto");

        applyModes(state);
        saveState(state);
    }

    private void scheduleDevices(SystemState state, LocalTime onTime, LocalTime offTime) {
        // You can store these times or use them later for scheduling
        state.setLightsOn(true);
        state.setPumpsOn(true);
        state.setBlowersOn(true);
        state.setFansOn(true);
        state.setFogInducerOn(true);
    }

    public void applyModes(SystemState state) {
        boolean cycleDefined = state.isGrowCycle() || state.isBloomCycle();
        boolean powerOn = state.isGeneralPower();

        state.setLightsOn(powerOn && "On".equals(state.getLightsMode()));
        state.setPumpsOn(powerOn && "On".equals(state.getPumpsMode()));
        state.setBlowersOn(powerOn && "On".equals(state.getBlowersMode()));
        state.setFansOn(powerOn && "On".equals(state.getFansMode()));
        state.setFogInducerOn(powerOn && "On".equals(state.getFogInducerMode()));
        state.setHeaterOn(powerOn && "On".equals(state.getHeaterMode()));
        state.setAirVentsOn(powerOn && "On".equals(state.getAirVentsMode()));

        if (!cycleDefined) {
            if ("Auto".equals(state.getLightsMode())) state.setLightsMode("Off");
            if ("Auto".equals(state.getPumpsMode())) state.setPumpsMode("Off");
            if ("Auto".equals(state.getBlowersMode())) state.setBlowersMode("Off");
            if ("Auto".equals(state.getFansMode())) state.setFansMode("Off");
            if ("Auto".equals(state.getFogInducerMode())) state.setFogInducerMode("Off");
            if ("Auto".equals(state.getHeaterMode())) state.setHeaterMode("Off");
            if ("Auto".equals(state.getAirVentsMode())) state.setAirVentsMode("Off");
        }
    }
    public SystemState save(SystemState state) {
        return repository.save(state);
    }
}
