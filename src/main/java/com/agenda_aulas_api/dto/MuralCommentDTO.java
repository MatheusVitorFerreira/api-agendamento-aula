package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.MuralComment;
import com.agenda_aulas_api.dto.record.MuralCommentResponseRecordDTO;

public class MuralCommentDTO {

    public static MuralCommentResponseRecordDTO fromMuralComment(MuralComment comment) {
        if (comment == null) return null;

        return new MuralCommentResponseRecordDTO(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getAuthor() != null ? comment.getAuthor().getUserId() : null,
                comment.getAuthor() != null ? comment.getAuthor().getUsername() : null
        );
    }


    public static MuralComment toEntity(MuralCommentResponseRecordDTO dto) {
        if (dto == null) return null;

        MuralComment comment = new MuralComment();
        comment.setId(dto.id());
        comment.setText(dto.text());
        comment.setCreatedAt(dto.createdAt());
        return comment;
    }
}
