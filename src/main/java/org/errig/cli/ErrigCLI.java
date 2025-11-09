package org.errig.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ErrigCLI {

    @Autowired
    private DeviceManagerCLI deviceManagerCLI;

    public void launch() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n🌿 Welcome back to E-rrig!");
            System.out.println("Thank you again for choosing E-rrig — Let's make the world greener!");
            System.out.println("Please choose an option:");
            System.out.println("1 - Device Manager");
            System.out.println("2 - Cycle Manager");
            System.out.println("3 - Exit");
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.println("🔧 Opening Device Manager...");
                    boolean backToMain = deviceManagerCLI.launch(scanner);
                    if (backToMain) continue;
                    break;
                case "2":
                    System.out.println("🌱 Opening Cycle Manager...");
                    // TODO: Launch cycle manager
                    break;
                case "3":
                    System.out.println("👋 Goodbye. Stay green!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("❓ Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
}