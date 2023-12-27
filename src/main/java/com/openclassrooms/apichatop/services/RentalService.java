package com.openclassrooms.apichatop.services;

import com.openclassrooms.apichatop.dto.CreateRentalDto;
import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.RentalRepository;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final S3StorageRepository storageRepository;

    public RentalService(RentalRepository rentalRepository, S3StorageRepository storageRepository) {
        this.rentalRepository = rentalRepository;
        this.storageRepository = storageRepository;
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

    public Rental createRental(CreateRentalDto newRentalDto, User user, MultipartFile picture) {
        Rental newRental = new Rental();
        newRental.setName(newRentalDto.getName());
        newRental.setSurface(newRentalDto.getSurface());
        newRental.setPrice(newRentalDto.getPrice());
        newRental.setDescription(newRentalDto.getDescription());
        newRental.setOwner_id(user.getId());
        newRental.setCreated_at(new Timestamp(System.currentTimeMillis()));
        newRental.setUpdated_at(newRental.getCreated_at());

        try {
            String imageURL = storageRepository.uploadFile(picture);
            newRental.setPicture(imageURL);

            Rental savedRental = rentalRepository.save(newRental);
            return savedRental;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
        }
    }
}
