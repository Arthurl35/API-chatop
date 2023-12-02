package com.openclassrooms.apichatop.services;

import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.repository.RentalRepository;
import org.springframework.stereotype.Service;


@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Iterable<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }
}
