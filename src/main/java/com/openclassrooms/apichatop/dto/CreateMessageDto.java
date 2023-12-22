package com.openclassrooms.apichatop.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateMessageDto {
    @NonNull
    private String message;

    @NonNull
    private Integer rental_id;
    
}
