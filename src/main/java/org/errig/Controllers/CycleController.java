package org.errig.Controllers;

import org.errig.Entities.SystemState;
import org.errig.Services.SystemStateService;
import org.errig.cycles.CycleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@Controller
@RequestMapping("/cycles")
public class CycleController {

    @Autowired
    private SystemStateService systemStateService;
    @Autowired
    private CycleManager cycleManager;


    // Show recent cycle logs
    @GetMapping("")
    public String cycles(Model model) {
        model.addAttribute("cycleLogs", systemStateService.getRecentCycleLogs());
        return "history/cycleLogHistory";
    }
    @PostMapping("/cycle/update")
    public String updateCycle(@RequestParam String cycleType,
                              @RequestParam LocalTime startTime,
                              @RequestParam LocalTime endTime,
                              @RequestParam(defaultValue = "false") boolean constrain) {

        SystemState state = systemStateService.getLatestState();

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

        state.setAutoOnTime(startTime);
        state.setAutoOffTime(endTime);

        // ✅ Use the injected bean, not the class
        cycleManager.applyCycleProfile(state, constrain);

        systemStateService.applyModes(state);
        systemStateService.save(state);
        systemStateService.logCycleState(state);

        return "redirect:/settings/cycle";
    }



    // Activate a cycle (delegates to service)
    @PostMapping("/activate")
    public String activateCycle(@RequestParam String cycleType) {
        SystemState state = systemStateService.getLatestState();

        if ("Grow".equalsIgnoreCase(cycleType)) {
            state.setGrowCycle(true);
            state.setBloomCycle(false);
        } else if ("Bloom".equalsIgnoreCase(cycleType)) {
            state.setGrowCycle(false);
            state.setBloomCycle(true);
        }

        systemStateService.startCycle(state);
        return "redirect:/history/cycles";
    }

    // 🛑 Stop the current cycle (delegates to service)
    @PostMapping("/stop")
    public String stopCycle() {
        SystemState state = systemStateService.getLatestState();
        systemStateService.stopCycle(state);
        return "redirect:/history/cycles";
    }
}