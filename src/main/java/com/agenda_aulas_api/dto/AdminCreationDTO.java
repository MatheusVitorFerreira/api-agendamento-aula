package com.agenda_aulas_api.dto;


import com.agenda_aulas_api.domain.UserType;

import lombok.Data;

@Data
public class AdminCreationDTO {
    private String username;
    private String password;
    private UserType userType;

}