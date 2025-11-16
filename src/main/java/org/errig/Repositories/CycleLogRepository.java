package org.errig.Repositories;

import org.errig.Entities.Actuators.CycleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CycleLogRepository extends JpaRepository<CycleLog, Long> {

    // Get the most recent log entry
    CycleLog findTopByOrderByUpdatedTsDesc();

    // Get the last 50 log entries
    List<CycleLog> findTop50ByOrderByUpdatedTsDesc();
}
