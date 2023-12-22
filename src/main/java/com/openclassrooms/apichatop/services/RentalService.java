package com.openclassrooms.apichatop.services;

import com.openclassrooms.apichatop.configuration.AwsS3Config;
import com.openclassrooms.apichatop.dto.CreateRentalDto;
import com.openclassrooms.apichatop.model.Rental;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.RentalRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;
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
    
    private String BUCKET_NAME = "api-chatop";
    private String region = "eu-west-3";

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
            String fileName = picture.getOriginalFilename();
    
            // Configuration du client S3 avec le point d'accès
            S3Client s3Client = AwsS3Config.createS3ClientWithAccessPoint();
    
            // Envoi de l'image vers le compartiment S3
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .build(), RequestBody.fromBytes(picture.getBytes()));
    
            // Génération de l'URL de l'image après l'avoir envoyée
            String imageURL = "https://api-chatop.s3." + region + ".amazonaws.com/" + BUCKET_NAME + "/" + fileName;
    
            // Enregistrement de l'URL de l'image dans la base de données
            newRental.setPicture(imageURL);
    
            // Sauvegarde de la nouvelle location dans la base de données
            Rental savedRental = rentalRepository.save(newRental);
            return savedRental;
    
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception en fonction de vos besoins
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
        }
    }
    
    
    

    

    
}
