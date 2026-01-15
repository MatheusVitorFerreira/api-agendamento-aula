package com.agenda_aulas_api.dto.record;

import java.util.UUID;

public record MuralCommentRequestRecordDTO(
        UUID postId,
        String text
) {}
