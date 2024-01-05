package com.openclassrooms.apichatop.services;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.openclassrooms.apichatop.dto.CreateMessageDto;
import com.openclassrooms.apichatop.model.Message;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


/**
 * The function creates a new message object with the provided messageDto and user information and
 * saves it in the message repository.
 * 
 * @param messageDto The `messageDto` parameter is an object of type `CreateMessageDto`. It contains
 * the necessary information to create a new message, such as the message content and the rental ID.
 * @param user The `user` parameter is an instance of the `User` class. It represents the user who is
 * creating the message.
 * @return The method is returning a Message object.
 */
    public Message createMessage(CreateMessageDto messageDto, User user) {
        Message message = new Message();
        message.setMessage(messageDto.getMessage());
        message.setCreated_at(new Timestamp(System.currentTimeMillis()));
        message.setUserId(user.getId());
        message.setRentalId(messageDto.getRental_id());

        return messageRepository.save(message);
    }
}
