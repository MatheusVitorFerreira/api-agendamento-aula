package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.record.LessonRequestRecordDTO;
import com.agenda_aulas_api.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllLessons() {
        List<Map<String, Object>> lessons = lessonService.findAll();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonRequestRecordDTO> getLessonById(@PathVariable UUID id) {
        LessonRequestRecordDTO lessonDTO = lessonService.findById(id);
        return ResponseEntity.ok(lessonDTO);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<LessonRequestRecordDTO>> getPageLessons(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "idLesson") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Page<LessonRequestRecordDTO> lessonPage = lessonService.findPageLesson(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(lessonPage);
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MODERATOR')")
    public ResponseEntity<LessonRequestRecordDTO> createLesson(
            @Valid @RequestBody LessonRequestRecordDTO lessonRequestDTO) {

        LessonRequestRecordDTO createdLesson = lessonService.createLesson(lessonRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdLesson.idLesson())
                .toUri();

        return ResponseEntity.created(location).body(createdLesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonRequestRecordDTO> updateLesson(
            @Valid @RequestBody LessonRequestRecordDTO lessonRequestDTO,
            @PathVariable UUID id) {

        LessonRequestRecordDTO updatedLesson = lessonService.updateLesson(lessonRequestDTO, id);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable UUID id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
