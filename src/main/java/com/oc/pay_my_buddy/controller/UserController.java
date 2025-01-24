package com.oc.pay_my_buddy.controller;

import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        // Ajouter la liste des utilisateurs au modèle
        model.addAttribute("users", userService.getAllUser());
        return "user/index"; // Renvoie le nom du template Thymeleaf (users.html)
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
