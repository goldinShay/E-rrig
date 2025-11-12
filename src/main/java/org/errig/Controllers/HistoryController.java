package org.errig.Controllers;

import org.errig.Entities.SensorLog;
import org.errig.Repositories.SensorLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private SensorLogRepository sensorLogRepository;

    @GetMapping("/history")
    public String showHistory(Model model) {
        List<SensorLog> logs = sensorLogRepository.findAllByOrderByTimestampDesc();
        model.addAttribute("logs", logs);
        return "history";
    }

    @PostMapping("/history/clear")
    public String clearHistory(@RequestParam String password, Model model) {
        if (!"growSecure".equals(password)) {
            model.addAttribute("errorMessage", "❌ Invalid password.");
            List<SensorLog> logs = sensorLogRepository.findAllByOrderByTimestampDesc();
            model.addAttribute("logs", logs);
            return "history";
        }

        sensorLogRepository.deleteAll();
        return "redirect:/history";
    }
}

