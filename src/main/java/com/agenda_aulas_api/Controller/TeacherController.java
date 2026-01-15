package com.agenda_aulas_api.Controller;


import com.agenda_aulas_api.domain.Teacher;
import com.agenda_aulas_api.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> findAll() {
        return ResponseEntity.ok(teacherService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Teacher> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(teacherService.findById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Teacher> update(
            @PathVariable UUID id,
            @RequestBody Teacher teacher
    ) {
        return ResponseEntity.ok(teacherService.update(id, teacher));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}