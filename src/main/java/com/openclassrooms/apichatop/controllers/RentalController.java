package com.openclassrooms.apichatop.controllers;

import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.services.RentalService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
 
    private RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Read - Get all rentals
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

}
