package com.openclassrooms.apichatop.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterDto {
    @NonNull
    private String email;

    @NonNull
    private String name;

    @NonNull
    private String password;
    
}
