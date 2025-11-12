package org.errig.cli.device;

import org.errig.Entities.*;
import org.errig.Services.SystemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@Component
public class UpdateDeviceCLI {

    @Autowired
    private SystemStateService systemStateService;

    public boolean launch(Scanner scanner) {
        SystemState state = systemStateService.getLatestState();
        List<LEDLight> devices = state.getLedLights();

        if (devices.isEmpty()) {
            System.out.println("⚠️ No devices found.");
            return false;
        }

        System.out.print("Enter device uniqueID to update (or 0 to return): ");
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

        String newName = prompt(scanner, "New name", target.getName());
        String newBrand = prompt(scanner, "New brand", target.getBrand());
        String newNote = prompt(scanner, "New note", target.getNote());

        LEDModel newModel = target.getModel();
        System.out.println("Current model: " + newModel.name());
        System.out.println("Press Enter to keep or choose new:");
        LEDModel[] models = LEDModel.values();
        for (int i = 0; i < models.length; i++) {
            System.out.printf("%d - %s%n", i + 1, models[i].name());
        }
        System.out.print("> ");
        String modelInput = scanner.nextLine().trim();
        if (!modelInput.isEmpty()) {
            try {
                int choice = Integer.parseInt(modelInput);
                if (choice >= 1 && choice <= models.length) {
                    newModel = models[choice - 1];
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid model. Keeping current.");
            }
        }

        System.out.println("\n📋 Review Updated Device:");
        System.out.println("Name: " + newName);
        System.out.println("Brand: " + newBrand);
        System.out.println("Model: " + newModel.name());
        System.out.println("Note: " + (newNote == null || newNote.isEmpty() ? "(none)" : newNote));

        System.out.println("\nConfirm update?");
        System.out.println("1 - Yes, Update");
        System.out.println("2 - Redo");
        System.out.println("3 - Back to Device Menu");
        System.out.println("0 - Back to Main Menu");
        System.out.print("> ");
        String confirm = scanner.nextLine().trim();

        switch (confirm) {
            case "1":
                target.setName(newName);
                target.setBrand(newBrand);
                target.setNote(newNote);
                target.setModel(newModel);
                target.setUpdatedTS(LocalDateTime.now());
                systemStateService.save(state);
                System.out.println("✅ Device updated.");
                return false;
            case "2":
                return launch(scanner); // redo
            case "3":
                return false;
            case "0":
                return true;
            default:
                System.out.println("❓ Invalid option. Returning to device menu.");
                return false;
        }
    }

    private String prompt(Scanner scanner, String label, String current) {
        System.out.printf("%s [%s]: ", label, current == null ? "(none)" : current);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? current : input;
    }
}