package com.openclassrooms.apichatop.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.apichatop.dto.CreateMessageDto;
import com.openclassrooms.apichatop.model.Message;
import com.openclassrooms.apichatop.model.User;
import com.openclassrooms.apichatop.services.AuthService;
import com.openclassrooms.apichatop.services.MessageService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;
    private AuthService authService;

    public MessageController(MessageService messageService, AuthService authService) {
        this.messageService = messageService;
        this.authService = authService;
    }

    @Operation(summary = "Create a message", description = "Create and send a message")
    @PostMapping("")
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody CreateMessageDto messageDto, Authentication authentication) {

        User user = authService.getLoggedInUser(authentication);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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


