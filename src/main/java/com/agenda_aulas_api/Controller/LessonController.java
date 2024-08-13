package com.agenda_aulas_api.Controller;


import com.agenda_aulas_api.dto.LessonDTO;
import com.agenda_aulas_api.dto.record.LessonRecord;
import com.agenda_aulas_api.exception.erros.DatabaseNegatedAccessException;
import com.agenda_aulas_api.exception.erros.NoAvailableSlotsException;
import com.agenda_aulas_api.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllDiscipline() {
        List<Map<String, Object>> lessonDTOList = lessonService.findAll();
        return ResponseEntity.ok(lessonDTOList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LessonDTO> getDisciplineById(@PathVariable UUID id) {
        LessonDTO LessonDTO = lessonService.findById(id);
        return ResponseEntity.ok(LessonDTO);
    }

    @PostMapping
    public ResponseEntity<LessonDTO> createDiscipline(@Valid @RequestBody LessonDTO lessonDTO) {
        LessonDTO LessonList = lessonService.createLesson(lessonDTO);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(lessonDTO.getIdLesson())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LessonDTO> updateLesson(@RequestBody LessonDTO lessonDTO, @PathVariable UUID id) {
        LessonDTO updateLesson = lessonService.updateLesson(lessonDTO, id);
        return ResponseEntity.ok(updateLesson);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<LessonDTO> deleteLesson(@PathVariable UUID id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lessonId}/students")
    public ResponseEntity<Void> addStudentToLesson(
            @PathVariable UUID lessonId,
            @RequestBody LessonRecord lessonRecord) {
        lessonService.addStudentToLesson(lessonId, lessonRecord);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-available-slots")
    public ResponseEntity<List<Map<String, Object>>> getLessonsWithoutAvailableSlots() {
        List<Map<String, Object>> lessonsWithoutAvailableSlots = lessonService.findLessonsWithoutAvailableSlots();
        return ResponseEntity.ok(lessonsWithoutAvailableSlots);
    }
}
