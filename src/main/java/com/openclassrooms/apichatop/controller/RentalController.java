package com.openclassrooms.apichatop.controller;

import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    /**
     * Read - Get all rentals
     * @return - A List of all rentals
     */
    @GetMapping("")
    public Iterable<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }
}
