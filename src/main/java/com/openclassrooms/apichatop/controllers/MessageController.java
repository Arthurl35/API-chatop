package com.openclassrooms.apichatop.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.apichatop.dto.CreateMessageDto;
import com.openclassrooms.apichatop.model.Message;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.services.MessageService;
import com.openclassrooms.apichatop.services.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;
    private UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> createMessage(@RequestBody CreateMessageDto messageDto, Authentication authentication) {

        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        // Récupérer le claim "email" du token JWT
        String userEmail = jwtToken.getClaimAsString("email");

        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.getUserByEmail(userEmail);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Message createdMessage = messageService.createMessage(messageDto, user);

        if (createdMessage == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Message send with success");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


