package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.LessonDTO;
import com.agenda_aulas_api.dto.record.LessonRequestRecordDTO;
import com.agenda_aulas_api.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        List<Map<String, Object>> lessonDTOList = lessonService.findAll();
        return ResponseEntity.ok(lessonDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable UUID id) {
        LessonDTO lessonDTO = lessonService.findById(id);
        return ResponseEntity.ok(lessonDTO);
    }

    @PostMapping
    public ResponseEntity<LessonRequestRecordDTO> createLesson(@Valid @RequestBody LessonRequestRecordDTO lessonRequestRecordDTO) {
        LessonRequestRecordDTO createdLesson = lessonService.createLesson(lessonRequestRecordDTO);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdLesson.toLesson().getIdLesson())
                .toUri();
        return ResponseEntity.created(headerLocation).body(createdLesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(@Valid @RequestBody LessonDTO lessonDTO, @PathVariable UUID id) {
        LessonDTO updatedLesson = lessonService.updateLesson(lessonDTO, id);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable UUID id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-available-slots")
    public ResponseEntity<List<Map<String, Object>>> getLessonsWithoutAvailableSlots() {
        List<Map<String, Object>> lessonsWithoutAvailableSlots = lessonService.findLessonsWithoutAvailableSlots();
        return ResponseEntity.ok(lessonsWithoutAvailableSlots);
    }
}
