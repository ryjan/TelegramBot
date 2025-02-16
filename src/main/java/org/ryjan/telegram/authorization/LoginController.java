package org.ryjan.telegram.authorization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/api/auth/login")
    public String showLoginPage() {
        return "forward:/login.html";
    }
}
