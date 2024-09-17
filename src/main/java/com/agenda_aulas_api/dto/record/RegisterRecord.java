package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Role;
import jakarta.validation.constraints.NotNull;

public record RegisterRecord (@NotNull String login, @NotNull String password, @NotNull Role role) {

}