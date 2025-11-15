package org.errig.Repositories;

import org.errig.Entities.CycleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CycleLogRepository extends JpaRepository<CycleLog, Long> {
    List<CycleLog> findTop50ByOrderByUpdatedTsDesc();
}
