package org.errig.Controllers;

import jakarta.validation.Valid;
import org.errig.Entities.LEDLight;
import org.errig.Entities.SensorLog;
import org.errig.Entities.SystemState;
import org.errig.Repositories.LEDLightRepository;
import org.errig.Repositories.SensorLogRepository;
import org.errig.Repositories.SystemStateRepository;
import org.errig.Services.SystemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class DashboardController {

    @Autowired
    private SystemStateService systemStateService;

    @Autowired
    private SystemStateRepository systemStateRepository;
    @Autowired
    private SensorLogRepository sensorLogRepository;
    @Autowired
    private LEDLightRepository ledLightRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            systemStateService.simulatePowerUse();
            SystemState state = systemStateService.getLatestState();
            model.addAttribute("state", state);

            SensorLog latestLog = sensorLogRepository.findTopByOrderByTimestampDesc();
            model.addAttribute("latestLog", latestLog);

            return "dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Dashboard failed: " + e.getMessage());
            return "error";
        }
    }


    @PostMapping("/dashboard/update")
    public String updateState(@Valid @ModelAttribute SystemState state, BindingResult result, Model model) {
        System.out.println("🔧 updateState() triggered");
        System.out.println("lightsMode: " + state.getLightsMode());

        if (result.hasErrors()) {
            System.out.println("❌ Binding errors:");
            result.getAllErrors().forEach(error -> System.out.println(error.toString()));
            model.addAttribute("errorMessage", "Invalid form submission.");
            return "dashboard";
        }

        try {
            SystemState latest = systemStateService.getLatestState();

            // 🔄 Update only the relevant field(s)
            latest.setLightsMode(state.getLightsMode());

            // 🧠 Recalculate actuator states
            systemStateService.applyModes(latest);

            // 💾 Persist updated state
            systemStateService.saveState(latest);

            System.out.println("💾 Lights mode updated and state saved.");
            return "redirect:/dashboard";
        } catch (Exception e) {
            System.out.println("🔥 Exception in updateState:");
            e.printStackTrace();
            model.addAttribute("errorMessage", "Update failed: " + e.getMessage());
            return "dashboard";
        }
    }
    @PostMapping("/dashboard/setCycle")
    public String setCycle(@RequestParam(required = false) Boolean growCycle,
                           @RequestParam(required = false) Boolean bloomCycle) {
        SystemState latest = systemStateService.getLatestState();
        latest.setGrowCycle(Boolean.TRUE.equals(growCycle));
        latest.setBloomCycle(Boolean.TRUE.equals(bloomCycle));
        systemStateService.saveState(latest);
        return "redirect:/dashboard";
    }
    @PostMapping("/dashboard/startCycle")
    public String startCycle(@ModelAttribute SystemState state, Model model) {
        try {
            SystemState latest = systemStateService.getLatestState();
            latest.setGrowCycle(state.isGrowCycle());
            latest.setBloomCycle(state.isBloomCycle());

            systemStateService.startCycle(latest);
            systemStateRepository.save(latest);
            System.out.println("Cycle start time: " + latest.getCycleStartTime());
            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Cycle start failed: " + e.getMessage());
            return "error";
        }
    }
    @PostMapping("/dashboard/updateLight")
    public String updateLight(@RequestParam String uniqueId,
                              @RequestParam String mode,
                              @RequestParam(required = false) String password,
                              Model model) {
        try {
            LEDLight light = ledLightRepository.findByUniqueId(uniqueId);
            if (light == null) {
                model.addAttribute("errorMessage", "Device not found.");
                return "error";
            }

            SystemState state = systemStateService.getLatestState();

            switch (mode.toLowerCase()) {
                case "on" -> {
                    light.setOn(true);
                    light.setMode("On");
                }
                case "off" -> {
                    light.setOn(false);
                    light.setMode("Off");
                }
                case "auto" -> {
                    if (!state.isGrowCycle() && !state.isBloomCycle()) {
                        model.addAttribute("errorMessage", "❌ Cannot enable Auto mode without an active cycle.");
                        return "error";
                    }

                    if (password == null || !password.equals("growSecure")) {
                        model.addAttribute("errorMessage", "❌ Invalid password for Auto mode.");
                        return "error";
                    }

                    light.setMode("Auto");
                    // Let the system decide whether it's currently On or Off
                    // based on time and cycle logic — not here
                }
            }

            light.setUpdatedTS(LocalDateTime.now());
            ledLightRepository.save(light);

            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Update failed: " + e.getMessage());
            return "error";
        }
    }
    @PostMapping("/dashboard/stopCycle")
    public String stopCycle() {
        SystemState latest = systemStateService.getLatestState();
        latest.setGrowCycle(false);
        latest.setBloomCycle(false);
        latest.setCycleStartTime(null);
        systemStateService.saveState(latest);
        return "redirect:/dashboard";
    }
}
