package com.agenda_aulas_api.dto.record;

public record LoginRequestDTO(
        String username,
        String password
) {}