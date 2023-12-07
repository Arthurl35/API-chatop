package com.openclassrooms.apichatop.services;

import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.RentalRepository;

import java.sql.Timestamp;
import java.util.Optional;

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

    public Optional<Rental> getRentalById(Long id) {
        return rentalRepository.findById(id);
    }

    public Optional<Rental> updateRental(Long id, Rental updatedRental) {
        return rentalRepository.findById(id).map(rental -> {
            if (updatedRental.getName() != null) {
                rental.setName(updatedRental.getName());
            }
            if (updatedRental.getSurface() != null) {
                rental.setSurface(updatedRental.getSurface());
            }
            if (updatedRental.getPrice() != null) {
                rental.setPrice(updatedRental.getPrice());
            }
            if (updatedRental.getDescription() != null) {
                rental.setDescription(updatedRental.getDescription());
            }
            rental.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            return rentalRepository.save(rental);
        });
    }
    
    public Rental createRental(Rental newRental, User user) {
        System.out.println("User ID: " + user.getId()); // Vérifie l'ID de l'utilisateur
        newRental.setOwner_id(user.getId());
        newRental.setCreated_at(new Timestamp(System.currentTimeMillis()));
        newRental.setUpdated_at(newRental.getCreated_at());
    
        System.out.println("New Rental Data: " + newRental.toString()); // Affiche les détails de la nouvelle location
    
        // Sauvegarde de la nouvelle location dans la base de données
        Rental savedRental = rentalRepository.save(newRental);
    
        if (savedRental != null) {
            System.out.println("Rental created successfully: " + savedRental.getId()); // Vérifie l'ID de la location sauvegardée
        } else {
            System.out.println("Failed to create rental"); // Affiche si la création a échoué
        }
    
        return savedRental;
    }
    
}
