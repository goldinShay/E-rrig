package org.errig.cli;

import org.errig.Entities.Actuators.LEDLight;
import org.errig.Entities.SystemState;
import org.errig.Repositories.LEDLightRepository;
import org.errig.Services.SystemStateService;
import org.errig.cli.device.AddDeviceCLI;
import org.errig.cli.device.RemoveDeviceCLI;
import org.errig.cli.device.UpdateDeviceCLI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class DeviceManagerCLI {

    @Autowired
    private SystemStateService systemStateService;
    @Autowired
    private UpdateDeviceCLI updateDeviceCLI;
    @Autowired
    private AddDeviceCLI addDeviceCLI;
    @Autowired
    private RemoveDeviceCLI removeDeviceCLI;
    @Autowired
    private LEDLightRepository ledLightRepository;



    public boolean launch(Scanner scanner) {
        while (true) {
            System.out.println("\n🔧 Device Manager");
            displayDevices();

            System.out.println("\nPlease choose an option:");
            System.out.println("1 - Add a new Device");
            System.out.println("2 - Update an existing Device");
            System.out.println("3 - Remove Device");
            System.out.println("4 - Test a Device");
            System.out.println("0 - Back");

            System.out.print("> ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    if (addDeviceCLI.launch(scanner)) return true;
                    break;
                case "2":
                    if (updateDeviceCLI.launch(scanner)) return true;
                    break;
                case "3":
                    if (removeDeviceCLI.launch(scanner)) return true;
                    break;
                case "4":
                    if (handleDeviceTest(scanner)) return true;
                    break;
                case "0":
                    System.out.println("🔙 Returning to Main Menu...");
                    return true;
                default:
                    System.out.println("❓ Invalid option. Please choose 0–4.");
            }
        }
    }
    private void displayDevices() {
        SystemState state = systemStateService.getLatestState();
        List<LEDLight> devices = state.getLedLights();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

        System.out.println("deviceID | Name       | State | Brand       | Model                | UpdatedTS");
        System.out.println("---------|------------|-------|-------------|----------------------|---------------------");

        for (LEDLight device : devices) {
            String line = String.format("%8s | %-10s | %-5s | %-11s | %-20s | %s",
                    device.getUniqueId(),
                    device.getName(),
                    device.isOn() ? "On" : "Off",
                    device.getBrand(),
                    device.getModel().name(),
                    device.getUpdatedTS().format(formatter));
            System.out.println(line);
        }
    }
    private boolean handleDeviceTest(Scanner scanner) {
        System.out.print("Enter device uniqueID to test (or 0 to return): ");
        String testId = scanner.nextLine().trim();
        if (testId.equals("0")) return true;

        LEDLight target = ledLightRepository.findByUniqueId(testId);
        if (target == null) {
            System.out.println("❌ Device not found.");
            return false;
        }

        if (!target.isTestable()) {
            System.out.println("⚠️ Device is already ON. Cannot test.");
            return false;
        }

        target.beginTest();
        ledLightRepository.save(target); // Persist ON state for dashboard visibility

        try {
            Thread.sleep(30000); // 30 seconds
        } catch (InterruptedException e) {
            System.out.println("⚠️ Test interrupted.");
        }

        target.endTest();
        ledLightRepository.save(target); // Persist OFF state for dashboard sync

        return false;
    }
}
