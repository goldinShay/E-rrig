package org.errig.Repositories;

import org.errig.Entities.LEDLight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LEDLightRepository extends JpaRepository<LEDLight, String> {
    LEDLight findByUniqueId(String uniqueId);
}