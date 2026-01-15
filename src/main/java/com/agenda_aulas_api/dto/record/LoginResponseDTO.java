package com.agenda_aulas_api.dto.record;


public record LoginResponseDTO(
        String token,
        Long expiresIn
) {}