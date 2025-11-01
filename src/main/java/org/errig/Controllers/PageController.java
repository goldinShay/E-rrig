package org.errig.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/welcome")
    public String welcome(Model model, Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oauthUser) {
            String name = oauthUser.getAttribute("name");   // Full name from Google
            String email = oauthUser.getAttribute("email"); // Optional: email
            model.addAttribute("username", name != null ? name : email);
        } else {
            model.addAttribute("username", authentication.getName()); // fallback for manual login
        }

        return "welcome";
    }


    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/history")
    public String history() {
        return "history";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }
}
