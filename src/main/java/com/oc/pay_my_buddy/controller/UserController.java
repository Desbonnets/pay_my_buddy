package com.oc.pay_my_buddy.controller;

import com.oc.pay_my_buddy.config.SecurityConfig;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SecurityConfig securityConfig;

    public UserController(UserService userService, SecurityConfig securityConfig) {
        this.userService = userService;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/")
    public String getAllUsers(Model model) {
        // Ajouter la liste des utilisateurs au modèle
        model.addAttribute("users", userService.getAllUser());
        return "user/index"; // Renvoie le nom du template Thymeleaf (users.html)
    }

    @GetMapping("/new")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/new";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/new";
        }

        try {
            user.setPassword(this.securityConfig.passwordEncoder().encode(user.getPassword()));

            userService.createUser(user);
            return "redirect:/user/";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création de l'utilisateur.");
            return "user/new";
        }
    }
}
