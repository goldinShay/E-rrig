package org.errig.Services;

import org.errig.Entities.SensorLog;
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

    @Scheduled(fixedRate = 60000) // every 60 seconds
    public void logSensorData() {
        SensorLog log = new SensorLog();
        log.setMessageId(UUID.randomUUID().toString());
        log.setTimestamp(LocalDateTime.now());

        log.setGrowBloom(Math.random() < 0.5);
        log.setLightsOn(Math.random() < 0.5);
        log.setPumpActive(Math.random() < 0.5);
        log.setFanActive(Math.random() < 0.5);
        log.setBlowerActive(Math.random() < 0.5);
        log.setHeaterActive(Math.random() < 0.5);

        log.setPowerUse(Math.random() * 500);
        log.setAirTemp(18 + Math.random() * 10);
        log.setAirHum(30 + Math.random() * 40);
        log.setAirPres(950 + Math.random() * 50);
        log.setCO2ppm(400 + Math.random() * 200);

        log.setWaterTemp(18 + Math.random() * 5);
        log.setWaterPH(5.5 + Math.random() * 2);
        log.setWaterEC(500 + Math.random() * 300);
        log.setWaterLevel(Math.random() * 100);


        repository.save(log);
    }
}