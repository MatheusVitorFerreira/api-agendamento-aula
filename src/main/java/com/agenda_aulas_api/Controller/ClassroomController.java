package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.record.ClassroomRequestRecordDTO;
import com.agenda_aulas_api.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;


    @GetMapping
    public ResponseEntity<Page<ClassroomRequestRecordDTO>> findAllPaged(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<ClassroomRequestRecordDTO> list = classroomService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClassroomRequestRecordDTO> findById(@PathVariable UUID id) {
        ClassroomRequestRecordDTO dto = classroomService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ClassroomRequestRecordDTO> create(@Valid @RequestBody ClassroomRequestRecordDTO dto) {
        ClassroomRequestRecordDTO created = classroomService.createClassroom(dto);
        URI uri = URI.create("/api/classrooms/" + created.idClass());
        return ResponseEntity.created(uri).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClassroomRequestRecordDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ClassroomRequestRecordDTO dto) {

        ClassroomRequestRecordDTO updated = classroomService.updateClassroom(id, dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }
}
