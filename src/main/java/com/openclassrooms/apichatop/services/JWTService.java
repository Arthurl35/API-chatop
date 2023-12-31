package com.openclassrooms.apichatop.services;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import com.openclassrooms.apichatop.model.User;



@Service
public class JWTService {

    private JwtEncoder jwtEncoder;

    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }


/**
 * The function generates a JWT token for a given user with specified claims and returns it as a map.
 * 
 * @param user The `user` parameter is an object of type `User`. It represents the user for whom the
 * token is being generated. The `User` class likely has properties such as `name` and `email`, which
 * are used to populate the claims of the JWT (JSON Web Token).
 * @return The method is returning a Map<String, String> object that contains a single key-value pair.
 * The key is "token" and the value is the token generated by the method.
 */
    public Map<String, String> generateToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(user.getName())
                .claim("email", user.getEmail())
                .build();
        
        JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        Jwt jwt = this.jwtEncoder.encode(encoderParameters);
        
        // Création de l'objet contenant le token
        Map<String, String> tokenObject = new HashMap<>();
        tokenObject.put("token", jwt.getTokenValue());
        
        return tokenObject;
    }
}
