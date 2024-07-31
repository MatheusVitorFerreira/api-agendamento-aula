package com.agenda_aulas_api.Controller;


import com.agenda_aulas_api.dto.StudentDTO;
import com.agenda_aulas_api.dto.TeacherDTO;
import com.agenda_aulas_api.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> findAll() {
        List<Map<String, Object>> teacherSummaryDTOS = teacherService.findAll();
        return ResponseEntity.ok(teacherSummaryDTOS);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable UUID id) {
        TeacherDTO teacherDTO = teacherService.findById(id);
        return ResponseEntity.ok(teacherDTO);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<TeacherDTO>> findPageTeachers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linePerPage", defaultValue = "10") Integer linePerPage,
            @RequestParam(value = "orderBy", defaultValue = "idTeacher") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<TeacherDTO> teacherPage = teacherService.findPageTeacherDTO(page, linePerPage, orderBy, direction);
        return ResponseEntity.ok(teacherPage);
    }

    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO saveTeacher = teacherService.createTeacher(teacherDTO);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveTeacher.getIdTeacher())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@RequestBody @Valid TeacherDTO teacherDTO, @PathVariable UUID id) {
        TeacherDTO updatedTeacher = teacherService.updateTeacher(teacherDTO, id);
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TeacherDTO> DeleteTeacher(@PathVariable UUID id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
