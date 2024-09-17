package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.UserType;
import jakarta.validation.constraints.NotNull;


public record CreateUserDTO(
        @NotNull String username,
        @NotNull String password,
        @NotNull UserType userType
) {

}