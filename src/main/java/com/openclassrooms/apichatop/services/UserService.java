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
    
/**
 * The function retrieves a user from the user repository based on their ID.
 * 
 * @param id The id parameter is of type Long and represents the unique identifier of the user that we
 * want to retrieve from the userRepository.
 * @return The method is returning a User object.
 */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
/**
 * The function `getUserByEmail` retrieves a user from the user repository based on their email.
 * 
 * @param email The email parameter is a string that represents the email address of the user you want
 * to retrieve from the userRepository.
 * @return The method is returning a User object.
 */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

/**
 * The function checks if a user with a given email exists in the system.
 * 
 * @param email The email parameter is a string that represents the email address of a user.
 * @return The method is returning a boolean value.
 */
    public boolean isUserExistByEmail(String email) {
        User existingUser = getUserByEmail(email);
        return existingUser != null;
    }

/**
 * The function checks if a given password matches the encoded password of a user.
 * 
 * @param user The `user` parameter is an object of type `User`. It represents the user for whom we
 * want to check the password.
 * @param password The password parameter is a String that represents the password that needs to be
 * checked.
 * @return The method is returning a boolean value.
 */
    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

/**
 * This function creates a new user with the provided email, name, and password, and stores it in the
 * database after performing necessary checks and security measures.
 * 
 * @param email The email parameter is a String that represents the email address of the user. It is
 * used to check if a user with the same email already exists in the system and to set the email field
 * of the new user.
 * @param name The name parameter is a String that represents the name of the user.
 * @param password The "password" parameter is a String that represents the password of the user.
 * @return The method is returning the newly created User object.
 */
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
