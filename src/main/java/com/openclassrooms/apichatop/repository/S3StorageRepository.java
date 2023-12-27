package com.openclassrooms.apichatop.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Repository
@PropertySource("classpath:aws.properties")
public class S3StorageRepository {

    private final S3Client s3Client;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.bucketName}")
    private String bucketName;


    public S3StorageRepository(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        try {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build(), RequestBody.fromBytes(file.getBytes()));

            // Génération de l'URL de l'image après l'avoir envoyée
            return generateImageURL(fileName);
        } catch (IOException e) {
            throw new IOException("Erreur lors de l'envoi du fichier : " + e.getMessage());
        }
    }

    private String generateImageURL(String fileName) {
        return "https://api-chatop.s3." + region + ".amazonaws.com/" + bucketName + "/" + fileName;
    }
}
