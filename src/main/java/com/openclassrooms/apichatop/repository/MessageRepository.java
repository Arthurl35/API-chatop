package com.openclassrooms.apichatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.apichatop.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}

