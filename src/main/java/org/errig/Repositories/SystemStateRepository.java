package org.errig.Repositories;

import org.errig.Entities.Actuators.SystemState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemStateRepository extends JpaRepository<SystemState, Long> {

    /**
     * Fetches the most recently saved SystemState.
     * Useful for retrieving the current state snapshot.
     */
    SystemState findTopByOrderByIdDesc();
}
