package org.errig.cli.device;

import org.errig.Entities.Actuators.LEDLight;
import org.errig.Entities.Actuators.SystemState;
import org.errig.Services.SystemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class RemoveDeviceCLI {

    @Autowired
    private SystemStateService systemStateService;

    public boolean launch(Scanner scanner) {
        SystemState state = systemStateService.getLatestState();
        List<LEDLight> devices = state.getLedLights();

        if (devices.isEmpty()) {
            System.out.println("⚠️ No devices to remove.");
            return false;
        }

        System.out.print("Enter device uniqueID to remove (or 0 to return): ");
        String input = scanner.nextLine().trim();
        if (input.equals("0")) return true;

        LEDLight target = devices.stream()
                .filter(d -> d.getUniqueId().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);

        if (target == null) {
            System.out.println("❌ Device not found.");
            return false;
        }

        System.out.println("\n📋 Device to be removed:");
        System.out.println("Name: " + target.getName());
        System.out.println("Brand: " + target.getBrand());
        System.out.println("Model: " + target.getModel().name());
        System.out.println("Note: " + (target.getNote() == null ? "(none)" : target.getNote()));

        System.out.println("\nAre you sure?");
        System.out.println("1 - Yes, remove this device");
        System.out.println("2 - Cancel");
        System.out.println("0 - Back to Main Menu");
        System.out.print("> ");
        String confirm = scanner.nextLine().trim();

        switch (confirm) {
            case "1":
                devices.remove(target);
                systemStateService.save(state);
                System.out.println("🗑️ Device removed successfully.");
                return false;
            case "2":
                System.out.println("❎ Removal cancelled.");
                return false;
            case "0":
                return true;
            default:
                System.out.println("❓ Invalid option. Returning to device menu.");
                return false;
        }
    }
}