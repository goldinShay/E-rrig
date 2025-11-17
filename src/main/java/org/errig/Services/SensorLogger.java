package org.errig.Services;

import org.errig.Entities.Sensors.SensorLog;
import org.errig.Repositories.SensorLogRepository;
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

    private final Random random = new Random();

    @Scheduled(fixedRate = 300000) // every 300 seconds (5min)
    public void logSensorData() {
        try {
            Long lastNumber = repository.findMaxMessageNumber();
            Long nextNumber = (lastNumber != null ? lastNumber : 0) + 1;

            SensorLog log = new SensorLog();
            log.setMessageNumber(nextNumber);
            log.setTimestamp(LocalDateTime.now());

            // Simulated system states
            log.setGrowBloom(random.nextBoolean());
            log.setLightsOn(random.nextBoolean());
            log.setPumpActive(random.nextBoolean());
            log.setFanActive(random.nextBoolean());
            log.setBlowerActive(random.nextBoolean());
            log.setHeaterActive(random.nextBoolean());

            // Simulated power and environment
            log.setPowerUse(round(random.nextDouble() * 500));
            log.setAirTemp(round(18 + random.nextDouble() * 10));
            log.setAirHum(round(30 + random.nextDouble() * 40));
            log.setAirPres(round(950 + random.nextDouble() * 50));
            log.setCO2ppm(round(400 + random.nextDouble() * 200));

            // Simulated water metrics
            log.setWaterTemp(round(18 + random.nextDouble() * 5));
            log.setWaterPH(round(5.5 + random.nextDouble() * 2));
            log.setWaterEC(round(500 + random.nextDouble() * 300));
            log.setWaterLevel(round(random.nextDouble() * 100));

            repository.save(log);
            System.out.println("✅ SensorLog saved: MSG# " + nextNumber);
        } catch (Exception e) {
            System.out.println("🔥 Failed to log sensor data:");
            e.printStackTrace();
        }
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}