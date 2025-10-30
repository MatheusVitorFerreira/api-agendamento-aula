package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.Material;

import java.util.UUID;
public record MaterialRequestDTO(
        UUID id,
        String fileName,
        String fileType,
        byte[] data,
        UUID parentId,
        String parentType
) {

    public Material toEntity() {
        return Material.builder()
                .id(this.id)
                .fileName(this.fileName)
                .fileType(this.fileType)
                .data(this.data)
                .parentId(this.parentId)
                .parentType(this.parentType)
                .build();
    }

    public static MaterialRequestDTO fromEntity(Material material) {
        return new MaterialRequestDTO(
                material.getId(),
                material.getFileName(),
                material.getFileType(),
                material.getData(),
                material.getParentId(),
                material.getParentType()
        );
    }
}