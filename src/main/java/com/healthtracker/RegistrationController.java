
package com.healthtracker;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthtracker.demo.model.AppUser;
import com.healthtracker.demo.repository.AppUserRepository;

@Controller
public class RegistrationController {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        username = username.trim();
        email = email.trim().toLowerCase();

        if (username.length() < 3 || password.length() < 8) {
            model.addAttribute(
                "error",
                "Username must have at least 3 characters and password at least 8."
            );
            return "register";
        }

        if (repository.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }

        if (repository.existsByEmail(email)) {
            model.addAttribute("error", "Email already registered.");
            return "register";
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        repository.save(user);

        return "redirect:/login?registered";
    }
}
