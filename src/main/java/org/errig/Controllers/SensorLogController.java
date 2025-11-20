package org.errig.Controllers;

import org.errig.Entities.Sensors.SensorLog;
import org.errig.Repositories.SensorLogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensorlogs")
public class SensorLogController {

    private final SensorLogRepository repository;

    public SensorLogController(SensorLogRepository repository) {
        this.repository = repository;
    }

    // ✅ Latest log for ticker
    @GetMapping("/latest")
    public SensorLog getLatestLog() {
        return repository.findTopByOrderByTimestampDesc();
    }

    // ✅ All logs for history table
    @GetMapping("/all")
    public List<SensorLog> getAllLogs() {
        return repository.findAllByOrderByTimestampDesc();
    }

    // ✅ Top 5 logs (optional, for quick preview)
    @GetMapping("/recent")
    public List<SensorLog> getRecentLogs() {
        return repository.findTop5ByOrderByTimestampDesc();
    }
}