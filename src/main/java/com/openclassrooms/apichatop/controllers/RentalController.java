package com.openclassrooms.apichatop.controllers;

import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.services.RentalService;
import com.openclassrooms.apichatop.services.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private RentalService rentalService;
    private UserService userService;

    public RentalController(RentalService rentalService,
            UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;

    }

    /**
     * Read - Get all rentals
     * 
     * @return - A List of all rentals
     */
    @GetMapping("")
    public Iterable<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Optional<Rental> rental = rentalService.getRentalById(id);
        return rental.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRental(@PathVariable Long id, @RequestBody Rental updatedRental) {
        Optional<Rental> updated = rentalService.updateRental(id, updatedRental);
        if (updated.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rental updated!");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createRental(@RequestBody Rental newRental, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        // Récupérer le claim "email" du token JWT
        String userEmail = jwtToken.getClaimAsString("email");

        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.getUserByEmail(userEmail);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Rental createdRental = rentalService.createRental(newRental, user);
        if (createdRental == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rental created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
