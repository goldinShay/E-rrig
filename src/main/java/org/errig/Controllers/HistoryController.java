package org.errig.Controllers;

import org.errig.Entities.SensorLog;
import org.errig.Entities.CycleLog;
import org.errig.Repositories.SensorLogRepository;
import org.errig.Repositories.CycleLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private SensorLogRepository sensorLogRepository;

    @Autowired
    private CycleLogRepository cycleLogRepository;

    // 🏠 Landing page
    @GetMapping("")
    public String historyMain() {
        return "history/historyMain";
        // templates/history/historyMain.html
    }

    // 📜 Sensor log history
    @GetMapping("/sensors")
    public String sensorsLogHistory(Model model) {
        List<SensorLog> logs = sensorLogRepository.findAllByOrderByTimestampDesc();
        model.addAttribute("logs", logs);
        return "history/sensorsLogHistory";
        // templates/history/sensorsLogHistory.html
    }

    // 🔐 Clear sensor log history
    @PostMapping("/sensors/clear")
    public String clearSensorHistory(@RequestParam String password, Model model) {
        if (!"growSecure".equals(password)) {
            model.addAttribute("errorMessage", "❌ Invalid password.");
            List<SensorLog> logs = sensorLogRepository.findAllByOrderByTimestampDesc();
            model.addAttribute("logs", logs);
            return "history/sensorsLogHistory";
        }
        sensorLogRepository.deleteAll();
        return "redirect:/history/sensors";
    }

    // 🌱 Cycle log history
    @GetMapping("/cycles")
    public String cycleLogHistory(Model model) {
        List<CycleLog> cycleLogs = cycleLogRepository.findTop50ByOrderByUpdatedTsDesc();
        model.addAttribute("cycleLogs", cycleLogs);
        return "history/cycleLogHistory";
        // templates/history/cycleLogHistory.html
    }

    // (Optional) clear cycle logs
    @PostMapping("/cycles/clear")
    public String clearCycleHistory(@RequestParam String password, Model model) {
        if (!"growSecure".equals(password)) {
            model.addAttribute("errorMessage", "❌ Invalid password.");
            List<CycleLog> cycleLogs = cycleLogRepository.findTop50ByOrderByUpdatedTsDesc();
            model.addAttribute("cycleLogs", cycleLogs);
            return "history/cycleLogHistory";
        }
        cycleLogRepository.deleteAll();
        return "redirect:/history/cycles";
    }
}
