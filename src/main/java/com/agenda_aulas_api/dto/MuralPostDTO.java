package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.MuralPost;
import com.agenda_aulas_api.dto.record.MuralPostResponseRecordDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MuralPostDTO {

    public static MuralPostResponseRecordDTO fromMuralPost(MuralPost post) {
        if (post == null) return null;

        List varComments = post.getComments() != null
                ? post.getComments().stream()
                .map(MuralCommentDTO::fromMuralComment)
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
                varComments
        );
    }


    public static MuralPost toEntity(MuralPostResponseRecordDTO dto) {
        if (dto == null) return null;

        MuralPost post = new MuralPost();
        post.setId(dto.id());
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setCreatedAt(dto.createdAt());
        post.setParentId(dto.parentId());
        post.setParentType(dto.parentType());
        return post;
    }
}
