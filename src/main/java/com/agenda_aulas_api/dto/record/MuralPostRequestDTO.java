package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.MuralPost;
import com.agenda_aulas_api.domain.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
public record MuralPostRequestDTO(
        UUID id,
        String title,
        String content,
        LocalDateTime createdAt,
        UUID parentId,
        String parentType,
        UUID authorId
) {

    public MuralPost toEntity() {
        MuralPost post = MuralPost.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .createdAt(this.createdAt != null ? this.createdAt : LocalDateTime.now())
                .parentId(this.parentId)
                .parentType(this.parentType)
                .build();

        if (authorId != null) {
            User author = new User();
            author.setUserID(authorId);
            post.setAuthor(author);
        }
        return post;
    }

    public static MuralPostRequestDTO fromEntity(MuralPost post) {
        return MuralPostRequestDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .parentId(post.getParentId())
                .parentType(post.getParentType())
                .authorId(post.getAuthor() != null ? post.getAuthor().getUserID() : null)
                .build();
    }
}