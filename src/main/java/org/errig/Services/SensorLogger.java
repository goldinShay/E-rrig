package org.errig.Services;

import org.errig.Entities.SensorLog;
import org.errig.Repositories.SensorLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class SensorLogger {

    @Autowired
    private SensorLogRepository repository;

    private final Random random = new Random();

    @Scheduled(fixedRate = 60000) // every 60 seconds
    public void logSensorData() {
        SensorLog log = new SensorLog();
        log.setTimestamp(LocalDateTime.now());

        // Simulated values
        log.setGrowBloom(random.nextBoolean());
        log.setLightsOn(random.nextBoolean());
        log.setPumpActive(random.nextBoolean());
        log.setFanActive(random.nextBoolean());
        log.setBlowerActive(random.nextBoolean());
        log.setHeaterActive(random.nextBoolean());

        log.setPowerUse(random.nextDouble() * 100); // watts
        log.setAirTemp(20 + random.nextDouble() * 5); // °C
        log.setAirHum(40 + random.nextDouble() * 20); // %
        log.setAirPres(1000 + random.nextDouble() * 20); // hPa
        log.setCO2ppm(400 + random.nextDouble() * 100); // ppm

        log.setWaterTemp(18 + random.nextDouble() * 4); // °C
        log.setWaterPH(5.5 + random.nextDouble() * 2); // pH
        log.setWaterEC(1.0 + random.nextDouble() * 1.5); // mS/cm
        log.setWaterLevel(50 + random.nextDouble() * 50); // %

        repository.save(log);
    }
}