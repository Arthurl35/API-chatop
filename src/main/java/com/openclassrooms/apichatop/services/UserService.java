package com.openclassrooms.apichatop.services;

import java.sql.Timestamp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.UserRepository;
@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isUserExistByEmail(String email) {
        User existingUser = getUserByEmail(email);
        return existingUser != null;
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User createUser(String email, String name, String password) {
        // Vérifier si l'utilisateur existe déjà avec cet email
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Créer un nouvel utilisateur
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        
        // Sécuriser et stocker le mot de passe de manière appropriée (par exemple, en utilisant BCrypt)
        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPassword(hashedPassword);

        // Gérer les timestamps pour la création et la mise à jour de l'utilisateur
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        newUser.setCreated_at(currentTimestamp);
        newUser.setUpdated_at(currentTimestamp);


        // Enregistrer le nouvel utilisateur dans la base de données
        userRepository.save(newUser);

        return newUser;
    }
   
}
