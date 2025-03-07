package com.oc.pay_my_buddy.controller;

import com.oc.pay_my_buddy.config.SecurityConfig;
import com.oc.pay_my_buddy.dto.Profil;
import com.oc.pay_my_buddy.dto.Relation;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SecurityConfig securityConfig;
    private final String pathNew = "user/new";
    private final Logger logger= LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, SecurityConfig securityConfig) {
        this.userService = userService;
        this.securityConfig = securityConfig;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public String getAllUsers(Model model) {
        // Ajouter la liste des utilisateurs au modèle
        model.addAttribute("users", userService.getAllUser());
        return "user/index"; // Renvoie le nom du template Thymeleaf (users.html)
    }

    @GetMapping("/new")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return this.pathNew;
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return this.pathNew;
        }

        try {
            user.setPassword(this.securityConfig.passwordEncoder().encode(user.getPassword()));
            userService.createUser(user);

            return "redirect:/user";

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création de l'utilisateur.");
            return this.pathNew;
        }
    }

    @GetMapping("/add_relation")
    @PreAuthorize("hasRole('USER')")
    public String showRelationForm(Model model) {
        model.addAttribute("relation", new Relation());
        return "user/add_relation";
    }

    @PostMapping("/add_relation")
    @PreAuthorize("hasRole('USER')")
    public String addRelation(
            @Valid @ModelAttribute("relation") Relation relation,
            BindingResult bindingResult,
            @AuthenticationPrincipal User userDetails,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "user/add_relation";
        }

        try {

            // Récupération de l'utilisateur connecté
//            User currentUser = userDetails;
//            User userConnected = userService.getUserByEmail(currentUser.getEmail());
            if (userDetails == null) {
                model.addAttribute("error", "Utilisateur connecté introuvable.");
                return "user/add_relation";
            }

            // Récupération de l'utilisateur à ajouter
            User friend = userService.getUserByEmail(relation.getEmail());
            if (friend == null) {
                model.addAttribute("error", "Utilisateur non trouvé.");
                return "user/add_relation";
            }

            // Vérifier si la relation existe déjà
            if (userDetails.getConnections().contains(friend)) {
                model.addAttribute("error", "Cette relation existe déjà.");
                return "user/add_relation";
            }

            // Ajouter la relation et sauvegarder
            userDetails.addConnection(friend);
            userService.updateUser(userDetails);

            return "redirect:/transaction";

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout de la relation.");
            logger.error(e.getMessage());
            return "user/add_relation";
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public String showUserProfile(@AuthenticationPrincipal User userDetails, Model model) {

        // Récupération de l'utilisateur connecté
//        User currentUser = userDetails;
//        User userConnected = userService.getUserByEmail(currentUser.getEmail());
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("profil", new Profil());
        model.addAttribute("user", userDetails);
        return "user/profile";
    }

    // Affichage du formulaire d'édition
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable int id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/error";
        }
        model.addAttribute("user", user);
        model.addAttribute("profil", new Profil());
        return "/user/edit";
    }

    // Traitement du formulaire de modification
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable int id,
                             @ModelAttribute("profil") @Valid Profil profil,
                             BindingResult result, Model model) {
        User user = userService.getUserById(id);
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "user/edit";
        }else {
            boolean resultUpdate = userService.updateUserProfil(id, profil);

            if (!resultUpdate) {
                model.addAttribute("user", user);
                model.addAttribute("error", "Erreur lors de la modification." );
                return "user/edit";
            }
        }

        return "redirect:/user/profile";
    }
}
