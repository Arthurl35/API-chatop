package com.openclassrooms.apichatop.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginDto {
    @NonNull
    private String login;

    @NonNull
    private String password;
}
