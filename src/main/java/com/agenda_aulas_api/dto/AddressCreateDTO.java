package com.agenda_aulas_api.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressCreateDTO {

    @NotBlank
    private String street;

    private Integer number;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String country;
}
