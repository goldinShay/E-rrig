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
            state = initializeDefaultState();
            repository.save(state);
        }

        if (latestLog != null) {
            state.setGeneralPower(true);
            state.setWaterLevelStatus(resolveWaterLevelStatus(latestLog.getWaterLevel()));
            state.setCurrentPowerUse(latestLog.getPowerUse());

            logCycleStatus(state);
            cycleManager.applyCycleProfile(state);
        } else {
            state.setGeneralPower(false);
            state.setWaterLevelStatus("Too Low");
            state.setCurrentPowerUse(0.0);
        }

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

        return state;
    }

    private String resolveWaterLevelStatus(double level) {
        if (level > 90) return "Too High";
        if (level > 60) return "Optimal";
        if (level > 30) return "Low";
        return "Too Low";
    }

    private void logCycleStatus(SystemState state) {
        if (state.isGrowCycle()) {
            System.out.println("Cycle: Grow");
        } else if (state.isBloomCycle()) {
            System.out.println("Cycle: Bloom");
        } else {
            System.out.println("Cycle: None");
        }
    }

    public void startCycle(SystemState state) {
        state.setCycleStartTime(LocalDateTime.now());

        // 🌱 Apply Grow or Bloom profile
        cycleManager.applyCycleProfile(state);

        // 🌿 Set all relevant actuators to Auto mode
        state.setLightsMode("Auto");
        state.setPumpsMode("Auto");
        state.setBlowersMode("Auto");
        state.setFansMode("Auto");
        state.setFogInducerMode("Auto");

        applyModes(state);
        saveState(state);
    }

    public void applyModes(SystemState state) {
        boolean cycleDefined = state.isGrowCycle() || state.isBloomCycle();
        boolean powerOn = Boolean.TRUE.equals(state.isGeneralPower());

        // 🌿 Handle Auto mode for lights
        if ("Auto".equals(safe(state.getLightsMode())) && cycleDefined && powerOn) {
            LocalTime now = LocalTime.now();
            LocalTime onTime = LocalTime.of(state.getAutoOnHour(), state.getAutoOnMinute());
            LocalTime offTime = LocalTime.of(state.getAutoOffHour(), state.getAutoOffMinute());

            boolean withinWindow = !now.isBefore(onTime) && !now.isAfter(offTime);
            state.setLightsOn(withinWindow);

            // 🔄 Sync LED lights in Auto mode
            for (LEDLight light : state.getLedLights()) {
                if ("Auto".equals(safe(light.getMode()))) {
                    light.setOn(withinWindow);
                    light.setUpdatedTS(LocalDateTime.now());
                    System.out.println("🔄 Syncing LED: " + light.getName() + " → " + (withinWindow ? "ON" : "OFF"));
                }
            }
        } else {
            state.setLightsOn(powerOn && "On".equals(safe(state.getLightsMode())));
        }

        // 🔧 Other actuators
        state.setPumpsOn(powerOn && "On".equals(safe(state.getPumpsMode())));
        state.setBlowersOn(powerOn && "On".equals(safe(state.getBlowersMode())));
        state.setFansOn(powerOn && "On".equals(safe(state.getFansMode())));
        state.setFogInducerOn(powerOn && "On".equals(safe(state.getFogInducerMode())));
        state.setHeaterOn(powerOn && "On".equals(safe(state.getHeaterMode())));
        state.setAirVentsOn(powerOn && "On".equals(safe(state.getAirVentsMode())));

        // 🧠 Debug output
        System.out.println("🧠 lightsMode: " + state.getLightsMode());
        System.out.println("🕒 now: " + LocalTime.now());
        System.out.println("🕒 onTime: " + LocalTime.of(state.getAutoOnHour(), state.getAutoOnMinute()));
        System.out.println("🕒 offTime: " + LocalTime.of(state.getAutoOffHour(), state.getAutoOffMinute()));
        System.out.println("💡 lightsOn: " + state.isLightsOn());

        // 🚫 Disable Auto modes if no cycle is active
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
}

