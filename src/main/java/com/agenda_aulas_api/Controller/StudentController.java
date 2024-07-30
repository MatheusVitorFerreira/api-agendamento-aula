package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.StudentDTO;
import com.agenda_aulas_api.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> findAll() {
        List<Map<String, Object>> students = studentService.findAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable UUID id) {
        StudentDTO studentDTO = studentService.findById(id);
        return ResponseEntity.ok(studentDTO);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Map<String, Object>>> findPageStudent(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "idStudent") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<Map<String, Object>> studentDTOS = studentService.findPageStudentDTO(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(studentDTOS);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody @Valid StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.ok(createdStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody @Valid StudentDTO studentDTO, @PathVariable UUID id) {
        StudentDTO updatedStudent = studentService.updateStudent(studentDTO, id);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentDTO> DeleteStudent(@PathVariable UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
