package com.agenda_aulas_api.Controller;



import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Map<String, Object>>> findPage(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "fullName") String orderBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return ResponseEntity.ok(
                studentService.findPage(page, size, orderBy, direction)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<Student> update(
            @PathVariable UUID id,
            @RequestBody Student student
    ) {
        return ResponseEntity.ok(studentService.update(id, student));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
