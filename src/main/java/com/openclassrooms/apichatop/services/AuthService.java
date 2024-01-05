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

/**
 * The function retrieves the logged-in user's email from the authentication token and uses it to fetch
 * the corresponding user object from the userService.
 * 
 * @param authentication The authentication parameter is an object that represents the current
 * authentication state of the user. It contains information about the user's credentials and
 * authorities.
 * @return The method is returning a User object.
 */
    public User getLoggedInUser(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userEmail = jwtToken.getClaimAsString("email");
        return userService.getUserByEmail(userEmail);
    }
}
