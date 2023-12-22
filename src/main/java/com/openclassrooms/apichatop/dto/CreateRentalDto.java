package com.openclassrooms.apichatop.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateRentalDto {
    @NonNull
    private String name;

    @NonNull
    private Double surface;

    @NonNull
    private Double price;

    @NonNull
    private String description;
}
