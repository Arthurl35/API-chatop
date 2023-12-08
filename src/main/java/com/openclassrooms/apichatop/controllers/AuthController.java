package com.openclassrooms.apichatop.controllers;


import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.apichatop.dto.UserDto;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.UserRepository;
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
    public ResponseEntity<String> getToken(@RequestBody Map<String, String> loginDetails) {
        String login = loginDetails.get("login");
        String password = loginDetails.get("password");

        // Vérifier si l'utilisateur existe dans la base de données avec le login donné
        User user = userService.getUserByEmail(login);

        if (user != null && userService.checkPassword(user, password)) {
            // Utilisateur trouvé et mot de passe correspondant
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(token);
        } else {
            // Utilisateur non trouvé ou mot de passe incorrect
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or incorrect credentials");
        }
    }
	
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> registrationDetails) {  
        String email = registrationDetails.get("email");
        String name = registrationDetails.get("name");
        String password = registrationDetails.get("password");
        if (email == null || name == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email, name, and password are required");
        }

        // Vérifier si l'utilisateur existe déjà avec cet email en utilisant le service
        boolean userExists = userService.isUserExistByEmail(email);
        if (userExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email already exists");
        }

        try {
            // Utiliser le service pour créer un nouvel utilisateur
            User newUser = userService.createUser(email, name, password);

            // Générer un token pour le nouvel utilisateur enregistré
            String token = jwtService.generateToken(newUser);

            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUserInfo(@RequestHeader("Authorization") String token) {
        String userEmail = jwtService.extractEmailFromToken(token);
        System.out.println(userEmail);
    
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