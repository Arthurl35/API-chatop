package com.openclassrooms.apichatop.controllers;


import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.UserRepository;
import com.openclassrooms.apichatop.services.JWTService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private JWTService jwtService;
	private UserRepository userRepository;
	
	public AuthController(JWTService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}
	
@PostMapping("/login")
    public ResponseEntity<String> getToken(@RequestBody Map<String, String> loginDetails) {
        String login = loginDetails.get("login");
        String password = loginDetails.get("password");

        // Vérifier si l'utilisateur existe dans la base de données avec le login donné
        User user = userRepository.findByEmail(login);

        if (user != null && password.equals(user.getPassword())) {
            // Utilisateur trouvé et mot de passe correspondant
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(token);
        } else {
            // Utilisateur non trouvé ou mot de passe incorrect
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or incorrect credentials");
        }
    }
	
}