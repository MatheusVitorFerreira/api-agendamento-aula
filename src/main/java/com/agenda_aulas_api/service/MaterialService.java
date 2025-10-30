package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Material;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.repository.MaterialRepository;
import com.agenda_aulas_api.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final LessonRepository lessonRepository;

    public Material uploadMaterial(UUID lessonId, MultipartFile file) throws IOException {
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada"));

        Material material = Material.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .data(file.getBytes())
                .parentId(lessonId)
                .parentType("LESSON")
                .build();

        return materialRepository.save(material);
    }

    public List<Material> listByLesson(UUID lessonId) {
        return materialRepository.findByParentIdAndParentType(lessonId, "LESSON");
    }


    public Material getMaterial(UUID id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));
    }

    public void deleteMaterial(UUID id) {
        materialRepository.deleteById(id);
    }
}
