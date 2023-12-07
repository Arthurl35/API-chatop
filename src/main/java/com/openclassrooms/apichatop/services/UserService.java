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
   
}
