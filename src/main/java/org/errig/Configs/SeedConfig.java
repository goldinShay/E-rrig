package org.errig.Configs;

import org.errig.Entities.Actuators.CycleLog;
import org.errig.Repositories.CycleLogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class SeedConfig {

    @Bean
    CommandLineRunner seedCycles(CycleLogRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                CycleLog grow = new CycleLog();
                grow.setCycleType("Grow");
                grow.setActive(false);
                grow.setUpdatedTs(LocalDateTime.now());
                repo.save(grow);

                CycleLog bloom = new CycleLog();
                bloom.setCycleType("Bloom");
                bloom.setActive(false);
                bloom.setUpdatedTs(LocalDateTime.now());
                repo.save(bloom);
            }
        };
    }
}

