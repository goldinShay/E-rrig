package org.errig.Repositories;

import org.errig.Entities.SystemState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemStateRepository extends JpaRepository<SystemState, Long> {

    /**
     * Fetches the most recently saved SystemState by ID.
     * Useful if IDs are strictly sequential.
     */
    Optional<SystemState> findTopByOrderByIdDesc();

    /**
     * Fetches the most recently saved SystemState by updated timestamp.
     * Preferred if you want to be explicit about "latest snapshot".
     */
    Optional<SystemState> findTopByOrderByUpdatedTsDesc();
}
