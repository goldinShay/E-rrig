package org.errig.Controllers;

import org.errig.Entities.Sensors.SensorLog;
import org.errig.Repositories.SensorLogRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SensorLogController {

    private final SensorLogRepository repository;

    public SensorLogController(SensorLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/latest")
    public SensorLog getLatestLog() {
        return repository.findTopByOrderByTimestampDesc();
    }
    @GetMapping("/all")
    public List<SensorLog> getAllLogs() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }

}

