package com.oc.pay_my_buddy.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;

@Controller
public class SecurityController {

    private final AuthenticationManager authenticationManager;

    public SecurityController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public record LoginRequest(String username, String password) {
    }

    public record LogoutRequest(String username) {}
}
