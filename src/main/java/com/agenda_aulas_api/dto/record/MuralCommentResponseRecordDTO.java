package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.MuralComment;
import java.time.LocalDateTime;
import java.util.UUID;

public record MuralCommentResponseRecordDTO(
        UUID id,
        String text,
        LocalDateTime createdAt,
        UUID authorId,
        String authorUsername
) {
    public static MuralCommentResponseRecordDTO fromEntity(MuralComment comment) {
        if (comment == null) return null;
        return new MuralCommentResponseRecordDTO(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getAuthor() != null ? comment.getAuthor().getUserId() : null,
                comment.getAuthor() != null ? comment.getAuthor().getUsername() : null
        );
    }
}
