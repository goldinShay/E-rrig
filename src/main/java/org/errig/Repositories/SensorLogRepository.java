package org.errig.Repositories;

import org.errig.Entities.Sensors.SensorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SensorLogRepository extends JpaRepository<SensorLog, Long> {

    // ✅ Latest log by timestamp
    SensorLog findTopByOrderByTimestampDesc();

    // ✅ Latest N logs by timestamp
    List<SensorLog> findTop5ByOrderByTimestampDesc();

    // ✅ All logs ordered by timestamp
    List<SensorLog> findAllByOrderByTimestampDesc();

    // ✅ Max message number (for manual numbering if needed)
    @Query("SELECT COALESCE(MAX(s.messageNumber), 0) FROM SensorLog s")
    Long findMaxMessageNumber();

    Optional<SensorLog> findTopByOrderByMessageNumberDesc();

}