package com.openclassrooms.apichatop.services;

import com.openclassrooms.apichatop.dto.CreateRentalDto;
import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.RentalRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


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
    
    public Rental createRental(CreateRentalDto newRentalDto, User user, MultipartFile picture) {
        Rental newRental = new Rental();
        newRental.setName(newRentalDto.getName());
        newRental.setSurface(newRentalDto.getSurface());
        newRental.setPrice(newRentalDto.getPrice());
        newRental.setDescription(newRentalDto.getDescription());
        newRental.setOwner_id(user.getId());
        newRental.setCreated_at(new Timestamp(System.currentTimeMillis()));
        newRental.setUpdated_at(newRental.getCreated_at());

        // Récupérer l'URL de l'image depuis le MultipartFile et stocker dans l'entité Rental
        try {
            String pictureUrl = savePictureAndGetUrl(picture);
            newRental.setPicture(pictureUrl);
        } catch (IOException e) {
            // Gérer l'exception en fonction de vos besoins
            e.printStackTrace();
        }

        // Sauvegarde de la nouvelle location dans la base de données
        Rental savedRental = rentalRepository.save(newRental);

        return savedRental;
    }

    // Méthode pour sauvegarder l'image et obtenir son URL
    private String savePictureAndGetUrl(MultipartFile pictureFile) throws IOException {
        // Pour l'exemple, je vais simplement retourner le nom du fichier comme URL
        String fileName = pictureFile.getOriginalFilename();
        return "http://localhost:9000/" + fileName; // À remplacer par la vraie URL
    }

    
}
