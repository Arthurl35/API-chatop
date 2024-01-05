package com.openclassrooms.apichatop.services;

import com.openclassrooms.apichatop.dto.CreateRentalDto;
import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.RentalRepository;
import com.openclassrooms.apichatop.repository.S3StorageRepository;

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

/**
 * The function returns all rentals from the rental repository.
 * 
 * @return The method is returning an Iterable of Rental objects.
 */
    public Iterable<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

/**
 * The function `getRentalById` retrieves a rental object from the rental repository based on the
 * provided ID.
 * 
 * @param id The id parameter is of type Long and represents the unique identifier of the rental that
 * we want to retrieve.
 * @return The method is returning an Optional object that contains a Rental object.
 */
    public Optional<Rental> getRentalById(Long id) {
        return rentalRepository.findById(id);
    }

/**
 * The function updates a rental object with the provided ID using the values from the updatedRental
 * object.
 * 
 * @param id The id parameter is the unique identifier of the rental that needs to be updated.
 * @param updatedRental The updatedRental parameter is an object of type Rental that contains the
 * updated values for the rental entity.
 * @return The method is returning an Optional object that contains either the updated Rental object if
 * it exists in the rentalRepository, or an empty Optional if the rental with the given id does not
 * exist.
 */
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

/**
 * The function creates a new rental object, sets its properties based on the provided DTO and user
 * information, uploads a picture file, and saves the rental object in the database.
 * 
 * @param newRentalDto An object of type CreateRentalDto that contains the details of the new rental to
 * be created. It likely has properties such as name, surface, price, description, etc.
 * @param user The "user" parameter is an instance of the User class, which represents the user who is
 * creating the rental. It contains information about the user, such as their ID, name, email, etc.
 * @param picture The `picture` parameter is of type `MultipartFile` and represents the image file that
 * is uploaded for the rental.
 * @return The method is returning a Rental object.
 */
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
