package org.errig.Services;

import org.errig.Entities.SystemState;
import org.errig.Repositories.SystemStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class SystemStateService {

    @Autowired
    private SystemStateRepository repository;

    public SystemState getLatestState() {
        SystemState state = repository.findTopByOrderByIdDesc();
        if (state == null) {
            state = new SystemState();
            state.setCycleMode("Grow");
            state.setGeneralPower(true);
            state.setWaterLevelStatus("Optimal");
            state.setLightsMode("Auto");
            state.setPumpsMode("Auto");
            state.setBlowersMode("Auto");
            state.setFansMode("Auto");
            state.setFogInducerMode("Auto");
            state.setHeaterMode("Auto");
            state.setAirVentsMode("Auto");
            state.setCurrentPowerUse(0.0);
            repository.save(state);
        }
        return state;
    }

    public SystemState saveState(SystemState state) {
        return repository.save(state);
    }

    // Optional: update specific fields later
    public void simulatePowerUse() {
        SystemState state = getLatestState();
        double simulated = 100 + Math.random() * 400; // Simulate 100–500 W
        state.setCurrentPowerUse(simulated);
        saveState(state);
    }
    public void startCycle(SystemState state) {
        LocalDateTime now = LocalDateTime.now();
        state.setCycleStartTime(now);

        if ("Grow".equals(state.getCycleMode())) {
            scheduleDevices(state, LocalTime.of(6, 0), LocalTime.of(23, 59));
        } else if ("Bloom".equals(state.getCycleMode())) {
            scheduleDevices(state, LocalTime.of(6, 0), LocalTime.of(18, 0));
        }
    }
    private void scheduleDevices(SystemState state, LocalTime onTime, LocalTime offTime) {
        // You can store these times or trigger logic here
        state.setLightsOn(true);
        state.setPumpsOn(true);
        state.setBlowersOn(true);
        state.setFansOn(true);
        state.setFogInducerOn(true);

        // Optionally: schedule off logic via a task or just store offTime for later
    }
    public void applyModes(SystemState state) {
        state.setLightsOn("On".equals(state.getLightsMode()));
        state.setPumpsOn("On".equals(state.getPumpsMode()));
        state.setBlowersOn("On".equals(state.getBlowersMode()));
        state.setFansOn("On".equals(state.getFansMode()));
        state.setFogInducerOn("On".equals(state.getFogInducerMode()));
        state.setHeaterOn("On".equals(state.getHeaterMode()));
        state.setAirVentsOn("On".equals(state.getAirVentsMode()));
    }


}
