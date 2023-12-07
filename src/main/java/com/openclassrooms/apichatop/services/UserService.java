package com.openclassrooms.apichatop.services;

import org.springframework.stereotype.Service;

import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.UserRepository;
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isUserExistByEmail(String email) {
        User existingUser = getUserByEmail(email);
        return existingUser != null;
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
        newUser.setPassword(password); // Assurez-vous de stocker les mots de passe de manière sécurisée dans la réalité

        // Enregistrer le nouvel utilisateur dans la base de données
        userRepository.save(newUser);

        return newUser;
    }
   
}
