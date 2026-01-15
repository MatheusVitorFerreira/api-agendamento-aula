package com.agenda_aulas_api.Controller;


import com.agenda_aulas_api.domain.Material;
import com.agenda_aulas_api.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping("/upload/{lessonId}")
    public ResponseEntity<Material> uploadFile(
            @PathVariable UUID lessonId,
            @RequestParam("file") MultipartFile file) throws IOException {

        Material saved = materialService.uploadMaterial(lessonId, file);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Material>> listByLesson(@PathVariable UUID lessonId) {
        return ResponseEntity.ok(materialService.listByLesson(lessonId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable UUID id) {
        Material material = materialService.getMaterial(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(material.getFileType()))
                .body(material.getData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable UUID id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
