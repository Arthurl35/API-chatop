package com.openclassrooms.apichatop.controllers;

import java.util.Collections;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.apichatop.dto.LoginDto;
import com.openclassrooms.apichatop.dto.RegisterDto;
import com.openclassrooms.apichatop.dto.UserDto;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.services.AuthService;
import com.openclassrooms.apichatop.services.JWTService;
import com.openclassrooms.apichatop.services.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private JWTService jwtService;
    private UserService userService;
    private AuthService authService;
    private ModelMapper modelMapper;

    public AuthController(JWTService jwtService, UserService userService, AuthService authService, ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get JWT token", description = "Authenticate user and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> getToken(@RequestBody LoginDto loginDetails) {
        String email = loginDetails.getEmail();
        String password = loginDetails.getPassword();
    
        // Vérifier si l'utilisateur existe dans la base de données avec le login donné
        User user = userService.getUserByEmail(email);
    
        if (user == null || !userService.checkPassword(user, password)) {
            // Utilisateur non trouvé ou mot de passe incorrect
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User not found or incorrect credentials"));
        }
    
        // Utilisateur trouvé et mot de passe correspondant
        Map<String, String> tokenObject = jwtService.generateToken(user);
        return ResponseEntity.ok(tokenObject);
    }
    
    @Operation(summary = "Register user", description = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterDto registrationDetails) {
        String email = registrationDetails.getEmail();
        String name = registrationDetails.getName();
        String password = registrationDetails.getPassword();
    
        // Vérifier si l'utilisateur existe déjà avec cet email en utilisant le service
        boolean userExists = userService.isUserExistByEmail(email);
        if (userExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "User with this email already exists"));
        }
    
        try {
            // Utiliser le service pour créer un nouvel utilisateur
            User newUser = userService.createUser(email, name, password);
    
            // Générer un token pour le nouvel utilisateur enregistré
            Map<String, String> tokenObject = jwtService.generateToken(newUser);
    
            return ResponseEntity.ok(tokenObject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    
    @Operation(summary = "Get logged-in user info", description = "Retrieve details of the logged-in user")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUserInfo(Authentication authentication) {

        User user = authService.getLoggedInUser(authentication);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        return ResponseEntity.ok(userDTO);
    }
}