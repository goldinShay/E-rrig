package org.errig.Services;

import org.errig.Entities.Sensors.SensorLog;
import org.errig.Entities.SystemState;
import org.errig.Repositories.SensorLogRepository;
import org.errig.Utilities.SensorLogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class SensorLogger {

    @Autowired
    private SensorLogRepository repository;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private PowerConsumptionManager powerConsumptionManager;
    @Autowired
    private SystemStateService systemStateService;




    private final Random random = new Random();

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void logSensorData() {
        try {
            Long lastNumber = repository.findMaxMessageNumber();
            Long nextNumber = (lastNumber != null ? lastNumber : 0) + 1;

            SensorLog log = new SensorLog();
            log.setMessageNumber(nextNumber);
            log.setTimestamp(LocalDateTime.now());

            // System states (still simulated until wired to real devices)
            log.setGrowBloom(random.nextBoolean());
            log.setLightsOn(random.nextBoolean());
            log.setPumpActive(random.nextBoolean());
            log.setFanActive(random.nextBoolean());
            log.setBlowerActive(random.nextBoolean());
            log.setHeaterActive(random.nextBoolean());

            // ✅ Live power consumption
            SystemState state = systemStateService.getLatestState();
            double livePowerUse = powerConsumptionManager.getTotalConsumption(state);
            log.setPowerUse(SensorLogUtils.roundToOneDecimalOrDefault(livePowerUse, 1.0));

            // Internal environment (simulated for now)
            log.setAirTemp(SensorLogUtils.roundToOneDecimal(18 + random.nextDouble() * 10));
            log.setAirHum(SensorLogUtils.roundToOneDecimal(30 + random.nextDouble() * 40));
            log.setAirPres(SensorLogUtils.roundToOneDecimal(950 + random.nextDouble() * 50));
            log.setCO2ppm(SensorLogUtils.roundToOneDecimal(400 + random.nextDouble() * 200));

            // Water metrics (simulated for now)
            log.setWaterTemp(SensorLogUtils.roundToOneDecimal(18 + random.nextDouble() * 5));
            log.setWaterPH(SensorLogUtils.roundToOneDecimalOrDefault(5.5 + random.nextDouble() * 2, 7.0));
            log.setWaterEC(SensorLogUtils.roundToOneDecimalOrDefault(500 + random.nextDouble() * 300, 1.0));
            log.setWaterLevel(SensorLogUtils.roundToOneDecimalOrDefault(random.nextDouble() * 100, 1.0));

            // ✅ External air temp (live Leiden weather)
            double liveExternalTemp = weatherService.fetchExternalAirTemp();
            log.setExternalAirTemp(SensorLogUtils.roundToOneDecimalOrDefault(liveExternalTemp, 15.0));

            repository.save(log);
            System.out.println("✅ SensorLog saved: MSG# " + nextNumber);
        } catch (Exception e) {
            System.out.println("🔥 Failed to log sensor data:");
            e.printStackTrace();
        }
    }


    /**
     * Safe rounding helper: returns a double, never null.
     */
    private double safeRound(double value, double fallback) {
        try {
            return Math.round(value * 100.0) / 100.0; // 2 decimals
        } catch (Exception e) {
            return fallback;
        }
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}