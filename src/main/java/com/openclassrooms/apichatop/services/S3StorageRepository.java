package com.openclassrooms.apichatop.services;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Repository
public class S3StorageRepository {

    private final S3Client s3Client;
    private static final String BUCKET_NAME = "api-chatop";
    private static final String REGION = "eu-west-3";

    public S3StorageRepository(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        try {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .build(), RequestBody.fromBytes(file.getBytes()));

            // Génération de l'URL de l'image après l'avoir envoyée
            return generateImageURL(fileName);
        } catch (IOException e) {
            throw new IOException("Erreur lors de l'envoi du fichier : " + e.getMessage());
        }
    }

    private String generateImageURL(String fileName) {
        return "https://api-chatop.s3." + REGION + ".amazonaws.com/" + BUCKET_NAME + "/" + fileName;
    }
}
