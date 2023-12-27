package com.openclassrooms.apichatop.configuration;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

        private static final String accessKeyId = "AKIAZCDJK6RGOLDE2PVJ"; 
        private static final String secretAccessKey = "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"; 
        private static final String region = "eu-west-3";
        private static final String endpoint = "https://api-chatoppublique-622991176780.s3-accesspoint.eu-west-3.amazonaws.com";
        
        @Bean
        public static S3Client createS3ClientWithAccessPoint() {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    
            return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .build();
        }
}
