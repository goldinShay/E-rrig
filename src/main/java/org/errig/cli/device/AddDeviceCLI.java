package org.errig.cli.device;

import org.errig.Entities.*;
import org.errig.Services.SystemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Scanner;

@Component
public class AddDeviceCLI {

    @Autowired
    private SystemStateService systemStateService;

    public boolean launch(Scanner scanner) {
        while (true) {
            System.out.println("\n➕ Add Device");

            DeviceType type = chooseDeviceType(scanner);
            if (type == null) return true;

            System.out.print("Enter device name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter brand: ");
            String brand = scanner.nextLine().trim();

            LEDModel model = null;
            if (type == DeviceType.LEDLight) {
                model = chooseLEDModel(scanner);
                if (model == null) return true;
            }

            System.out.print("Add a note (optional): ");
            String note = scanner.nextLine().trim();

            String id = DeviceIdGenerator.generate(type);
            System.out.println("\n📋 Review Device:");
            System.out.println("Type: " + type.name());
            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Brand: " + brand);
            if (model != null) System.out.println("Model: " + model.name());
            System.out.println("Note: " + (note.isEmpty() ? "(none)" : note));

            System.out.println("\nConfirm?");
            System.out.println("1 - Yes, Add");
            System.out.println("2 - Redo");
            System.out.println("0 - Back to Main Menu");
            System.out.print("> ");
            String confirm = scanner.nextLine().trim();

            switch (confirm) {
                case "1":
                    persistDevice(type, id, name, brand, model, note);
                    System.out.println("✅ Device added successfully!");
                    return false;
                case "2":
                    continue;
                case "0":
                    System.out.println("🔙 Returning to Main Menu...");
                    return true;
                default:
                    System.out.println("❓ Invalid option. Returning to main menu.");
                    return true;
            }
        }
    }

    private DeviceType chooseDeviceType(Scanner scanner) {
        DeviceType[] types = DeviceType.values();
        System.out.println("Choose device type:");
        for (int i = 0; i < types.length; i++) {
            System.out.printf("%d - %s%n", i + 1, types[i].name());
        }
        System.out.print("> ");
        String input = scanner.nextLine().trim();
        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > types.length) throw new IndexOutOfBoundsException();
            return types[choice - 1];
        } catch (Exception e) {
            System.out.println("❌ Invalid selection. Returning to main menu.");
            return null;
        }
    }

    private LEDModel chooseLEDModel(Scanner scanner) {
        LEDModel[] models = LEDModel.values();
        System.out.println("Choose LED model:");
        for (int i = 0; i < models.length; i++) {
            System.out.printf("%d - %s%n", i + 1, models[i].name());
        }
        System.out.print("> ");
        String input = scanner.nextLine().trim();
        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > models.length) throw new IndexOutOfBoundsException();
            return models[choice - 1];
        } catch (Exception e) {
            System.out.println("❌ Invalid selection. Returning to main menu.");
            return null;
        }
    }

    private void persistDevice(DeviceType type, String id, String name, String brand, LEDModel model, String note) {
        SystemState state = systemStateService.getLatestState();

        if (type == DeviceType.LEDLight) {
            LEDLight light = new LEDLight(model);
            light.setUniqueId(id);
            light.setName(name);
            light.setBrand(brand);
            light.setNote(note);
            light.setUpdatedTS(LocalDateTime.now());
            light.setOn(false);
            light.setCycleType("Grow");

            state.getLedLights().add(light);
            systemStateService.save(state);
        }

        // TODO: Add support for other device types
    }
}