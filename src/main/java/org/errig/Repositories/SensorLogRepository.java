package org.errig.Repositories;

import org.errig.Entities.SensorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  SensorLogRepository extends JpaRepository<SensorLog, Long> {
    SensorLog findTopByOrderByTimestampDesc();
    List<SensorLog> findTop5ByOrderByTimestampDesc();
    @Query("SELECT MAX(s.messageNumber) FROM SensorLog s")
    Long findMaxMessageNumber();


}
