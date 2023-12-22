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

    public Message createMessage(CreateMessageDto messageDto, User user) {
        Message message = new Message();
        message.setMessage(messageDto.getMessage());
        message.setCreated_at(new Timestamp(System.currentTimeMillis()));
        message.setUserId(user.getId());
        message.setRentalId(messageDto.getRental_id());

        return messageRepository.save(message);
    }
}
