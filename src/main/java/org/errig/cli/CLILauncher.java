package org.errig.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CLILauncher implements CommandLineRunner {
    @Autowired
    private ErrigCLI errigCLI;

    @Override
    public void run(String... args) {
        try {
            Thread.sleep(2000); // ⏳ Optional delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        errigCLI.launch(); // ✅ Spring-managed instance
    }
}