package com.oc.pay_my_buddy.controller;

import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.service.TransactionService;
import com.oc.pay_my_buddy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private TransactionService transactionService;
    private UserService userService;

    public TransactionController(
            TransactionService transactionService,
            UserService userService
    ) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public String getAllTransactions(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Ajouter la liste des utilisateurs au modèle
        User currentUser = (User) userDetails;
        currentUser = this.userService.getUserByEmail(currentUser.getEmail());

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("users", currentUser.getConnections());
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("transactions", transactionService.getTransactionsBySenderId(currentUser));
        return "/transaction/index";
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public String newTransaction(
            @Valid @ModelAttribute("transaction") Transaction transaction,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "transaction/index";
        }

        try {
            User currentUser = (User) userDetails;
            currentUser = this.userService.getUserByEmail(currentUser.getEmail());
            if (currentUser == null) {
                return "redirect:/login";
            }

            transaction.setSender(currentUser);
            transactionService.createTransaction(transaction);

            return "redirect:/transaction";

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création du transfert.");
            return "transaction/index";
        }
    }
}
