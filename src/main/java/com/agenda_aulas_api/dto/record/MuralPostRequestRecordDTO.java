package com.agenda_aulas_api.dto.record;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MuralPostRequestRecordDTO(
        String title,
        String content,
        UUID parentId,
        String parentType
) {}
