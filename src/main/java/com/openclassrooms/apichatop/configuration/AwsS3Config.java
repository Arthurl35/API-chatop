package com.openclassrooms.apichatop.configuration;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class AwsS3Config {

        // Récupérer les informations d'identification depuis le fichier de configuration
        private static final String accessKeyId = "AKIAZCDJK6RGOLDE2PVJ"; // Remplacez par le code d'accès AWS
        private static final String secretAccessKey = "G0sXol62EF9UUm+h294JCimhOQVMkq4oRmIL23bA"; // Remplacez par la clé secrète AWS
        private static final String region = "eu-west-3"; // Remplacez par la région AWS
        private static final String bucketName = "api-chatop"; // Remplacez par le nom du bucket S3
        private static final String endpoint = "https://api-chatoppublique-622991176780.s3-accesspoint.eu-west-3.amazonaws.com"; // ARN de votre point d'accès S3
        

        @Bean
        public static S3Client createS3ClientWithAccessPoint() {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    
            return S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .region(Region.of(region))
                    .endpointOverride(URI.create(endpoint)) // Spécifiez le point d'accès ici
                                .serviceConfiguration(S3Configuration.builder()
                .checksumValidationEnabled(false) // Désactive la validation de la somme de contrôle si nécessaire
                .build())
            .build();
        }
}
