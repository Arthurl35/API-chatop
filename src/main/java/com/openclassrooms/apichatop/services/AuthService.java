package com.openclassrooms.apichatop.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.openclassrooms.apichatop.model.User;

@Service
public class AuthService {
    private UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User getLoggedInUser(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userEmail = jwtToken.getClaimAsString("email");
        return userService.getUserByEmail(userEmail);
    }
}
