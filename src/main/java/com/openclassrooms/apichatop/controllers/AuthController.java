package com.openclassrooms.apichatop.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.apichatop.dto.LoginDto;
import com.openclassrooms.apichatop.dto.RegisterDto;
import com.openclassrooms.apichatop.dto.UserDto;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.services.JWTService;
import com.openclassrooms.apichatop.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private JWTService jwtService;
    private UserService userService;

    public AuthController(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

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

    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUserInfo(Authentication authentication) {

        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        // Récupérer le claim "email" du token JWT
        String userEmail = jwtToken.getClaimAsString("email");

        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.getUserByEmail(userEmail);
        

        if (user != null) {
            // Créez un UserDTO à partir des informations récupérées de l'utilisateur
            UserDto userDTO = new UserDto();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setCreated_at(user.getCreated_at());
            userDTO.setUpdated_at(user.getUpdated_at());

            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}