package com.openclassrooms.apichatop.controllers;

import com.openclassrooms.apichatop.dto.CreateRentalDto;
import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.services.AuthService;
import com.openclassrooms.apichatop.services.RentalService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private RentalService rentalService;
    private AuthService authService;

    public RentalController(RentalService rentalService, AuthService authService) {
        this.rentalService = rentalService;
        this.authService = authService;
    }

    @GetMapping("")
    public Map<String, Iterable<Rental>> getAllRentals() {
        Map<String, Iterable<Rental>> response = new HashMap<>();
        Iterable<Rental> rentals = rentalService.getAllRentals();
        response.put("rentals", rentals);
        return response;
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

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRental(
            CreateRentalDto newRental,
            @RequestPart("picture") MultipartFile picture,
            Authentication authentication) {
        
        User user = authService.getLoggedInUser(authentication);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Rental createdRental = rentalService.createRental(newRental, user, picture);
        if (createdRental == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rental created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
