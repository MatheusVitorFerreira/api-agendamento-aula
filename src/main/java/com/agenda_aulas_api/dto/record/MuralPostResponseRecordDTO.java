package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.MuralPost;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record MuralPostResponseRecordDTO(
        UUID id,
        String title,
        String content,
        LocalDateTime createdAt,
        UUID authorId,
        String authorUsername,
        UUID parentId,
        String parentType,
        List<MuralCommentResponseRecordDTO> comments
) {
    public static MuralPostResponseRecordDTO fromEntity(MuralPost post) {
        if (post == null) return null;

        List<MuralCommentResponseRecordDTO> comments = post.getComments() != null
                ? post.getComments().stream()
                .map(MuralCommentResponseRecordDTO::fromEntity)
                .collect(Collectors.toList())
                : List.of();

        return new MuralPostResponseRecordDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getAuthor() != null ? post.getAuthor().getUserId() : null,
                post.getAuthor() != null ? post.getAuthor().getUsername() : null,
                post.getParentId(),
                post.getParentType(),
                comments
        );
    }
}
