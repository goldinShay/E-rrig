package org.errig.Repositories;

import org.errig.Entities.SystemState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemStateRepository extends JpaRepository<SystemState, Long> {
    // Optional: fetch the latest state
    SystemState findTopByOrderByIdDesc();
}
