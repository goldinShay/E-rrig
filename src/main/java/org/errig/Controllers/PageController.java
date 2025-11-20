package org.errig.Controllers;

import org.errig.Entities.Sensors.SensorLog;
import org.errig.Repositories.SensorLogRepository;
import org.errig.Repositories.SystemStateRepository;
import org.errig.Services.SystemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        log.setAirTemp(safeRoundToOneDecimal(log.getAirTemp(), 0.0));
        log.setAirHum(safeRoundToOneDecimal(log.getAirHum(), 0.0));
        log.setAirPres(safeRoundToOneDecimal(log.getAirPres(), 0.0));
        log.setCO2ppm(safeRoundToOneDecimal(log.getCO2ppm(), 0.0));
        log.setWaterTemp(safeRoundToOneDecimal(log.getWaterTemp(), 20.0));
        log.setWaterPH(safeRoundToOneDecimal(log.getWaterPH(), 7.0));
        log.setWaterEC(safeRoundToOneDecimal(log.getWaterEC(), 1.0));   // force safe default
        log.setWaterLevel(safeRoundToOneDecimal(log.getWaterLevel(), 0.0));
        log.setExternalAirTemp(safeRoundToOneDecimal(log.getExternalAirTemp(), 0.0));
    }

    /**
     * Always returns a primitive double, never null.
     * Falls back to a safe default if anything goes wrong.
     */
    private double safeRoundToOneDecimal(Double value, double fallback) {
        try {
            return Math.round(value * 10.0) / 10.0;
        } catch (Exception e) {
            return fallback;
        }
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
