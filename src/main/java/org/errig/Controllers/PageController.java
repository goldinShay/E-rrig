package org.errig.Controllers;

import jakarta.validation.Valid;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("username", extractUsername(authentication));
        return "welcome";
    }

    @GetMapping("/history")
    public String history() {
        return "history";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    // 🔧 Utility methods

    private String extractUsername(Authentication authentication) {
        if (authentication == null) return "Guest";

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User oauthUser) {
            String name = oauthUser.getAttribute("name");
            String email = oauthUser.getAttribute("email");
            return name != null ? name : email;
        }
        return authentication.getName();
    }

    private void roundSensorValues(SensorLog log) {
        log.setAirTemp(roundToOneDecimal(log.getAirTemp()));
        log.setAirHum(roundToOneDecimal(log.getAirHum()));
        log.setAirPres(roundToOneDecimal(log.getAirPres()));
        log.setCO2ppm(roundToOneDecimal(log.getCO2ppm()));
        log.setWaterTemp(roundToOneDecimal(log.getWaterTemp()));
        log.setWaterPH(roundToOneDecimal(log.getWaterPH()));
        log.setWaterEC(roundToOneDecimal(log.getWaterEC()));
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
