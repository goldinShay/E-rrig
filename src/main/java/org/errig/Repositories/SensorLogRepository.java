package org.errig.Repositories;

import org.errig.Entities.SensorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  SensorLogRepository extends JpaRepository<SensorLog, Long> {
    SensorLog findTopByOrderByTimestampDesc();
}
