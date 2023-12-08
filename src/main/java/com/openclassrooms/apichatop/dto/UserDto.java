package com.openclassrooms.apichatop.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private Timestamp created_at;
    private Timestamp updated_at;
}
