package com.openclassrooms.apichatop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.openclassrooms.apichatop.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    public User findByEmail(String email);
}
