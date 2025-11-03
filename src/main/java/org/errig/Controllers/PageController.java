package org.errig.Controllers;

import org.errig.Entities.SensorLog;
import org.errig.Entities.SystemState;
import org.errig.Repositories.SensorLogRepository;
import org.errig.Repositories.SystemStateRepository;
import org.errig.Services.SystemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class PageController {

    @Autowired
    private SystemStateService systemStateService;

    @Autowired
    private SystemStateRepository systemStateRepository;

    @Autowired
    private SensorLogRepository sensorLogRepository;

    @GetMapping("/welcome")
    public String welcome(Model model, Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oauthUser) {
            String name = oauthUser.getAttribute("name");
            String email = oauthUser.getAttribute("email");
            model.addAttribute("username", name != null ? name : email);
        } else {
            model.addAttribute("username", authentication.getName());
        }

        return "welcome";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        try {
            systemStateService.simulatePowerUse();
            SystemState state = systemStateService.getLatestState();
            model.addAttribute("state", state);

            SensorLog latestLog = sensorLogRepository.findTopByOrderByTimestampDesc();
            if (latestLog != null) {
                latestLog.setAirTemp(roundToOneDecimal(latestLog.getAirTemp()));
                latestLog.setAirHum(roundToOneDecimal(latestLog.getAirHum()));
                latestLog.setAirPres(roundToOneDecimal(latestLog.getAirPres()));
                latestLog.setCO2ppm(roundToOneDecimal(latestLog.getCO2ppm()));
                latestLog.setWaterTemp(roundToOneDecimal(latestLog.getWaterTemp()));
                latestLog.setWaterPH(roundToOneDecimal(latestLog.getWaterPH()));
                latestLog.setWaterEC(roundToOneDecimal(latestLog.getWaterEC()));
            }
            model.addAttribute("latestLog", latestLog);

            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof OAuth2User oauthUser) {
                    String name = oauthUser.getAttribute("name");
                    String email = oauthUser.getAttribute("email");
                    model.addAttribute("username", name != null ? name : email);
                } else {
                    model.addAttribute("username", authentication.getName());
                }
            } else {
                model.addAttribute("username", "Guest");
            }

            return "dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/dashboard/update")
    public String updateState(@ModelAttribute SystemState state) {
        systemStateService.applyModes(state);
        systemStateRepository.save(state);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/startCycle")
    public String startCycle(@ModelAttribute SystemState state) {
        systemStateService.startCycle(state);
        systemStateRepository.save(state);
        return "redirect:/dashboard";
    }

    @GetMapping("/history")
    public String history() {
        return "history";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}