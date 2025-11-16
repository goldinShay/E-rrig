package org.errig.Controllers;

import org.errig.Entities.Actuators.SystemState;
import org.errig.Services.SystemStateService;
import org.errig.cycles.CycleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private SystemStateService systemStateService;

    @Autowired
    private CycleManager cycleManager;

    // Main settings dashboard
    @GetMapping("")
    public String settingsMain() {
        return "settings/settingsMain";
    }

    // Cycle settings page
    @GetMapping("/cycle")
    public String cycleSettings(Model model) {
        SystemState state = systemStateService.getLatestState();
        model.addAttribute("systemState", state);
        return "settings/cycleSettings";
    }

    // Device settings page (future expansion)
    @GetMapping("/device")
    public String deviceSettings() {
        return "settings/deviceSettings";
    }

    // 🔧 Handle cycle updates (from form submit)
    @PostMapping("/cycle/update")
    public String updateCycle(@RequestParam String cycleType,
                              @RequestParam LocalTime startTime,
                              @RequestParam LocalTime endTime,
                              @RequestParam(defaultValue = "false") boolean constrain) {

        // 🔄 Load current state
        SystemState state = systemStateService.getLatestState();

        // 🌱 Set cycle type
        if ("grow".equalsIgnoreCase(cycleType)) {
            state.setGrowCycle(true);
            state.setBloomCycle(false);
        } else if ("bloom".equalsIgnoreCase(cycleType)) {
            state.setGrowCycle(false);
            state.setBloomCycle(true);
        } else {
            state.setGrowCycle(false);
            state.setBloomCycle(false);
        }

        // 🕒 Update cycle times
        state.setAutoOnTime(startTime);
        state.setAutoOffTime(endTime);

        // ⚙️ Apply constraint logic
        cycleManager.applyCycleProfile(state, constrain);

        // 💾 Persist SystemState
        systemStateService.save(state);

        // 📝 Log into CycleLog history
        systemStateService.logCycleState(state);

        // 🔙 Redirect back to settings page
        return "redirect:/settings/cycle";
    }
}