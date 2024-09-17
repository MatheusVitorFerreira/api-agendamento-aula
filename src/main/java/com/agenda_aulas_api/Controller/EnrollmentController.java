package com.agenda_aulas_api.Controller;


import com.agenda_aulas_api.dto.EnrollmentDTO;
import com.agenda_aulas_api.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    @Autowired
    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollment() {
        List<EnrollmentDTO> EnrollmentList = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(EnrollmentList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable UUID id) {
        EnrollmentDTO enrollmentDTO = enrollmentService.findById(id);
        return ResponseEntity.ok(enrollmentDTO);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<EnrollmentDTO>> findPageEnrollment(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<EnrollmentDTO> enrollmentDTOPage =
                enrollmentService.findPageEnrollments(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(enrollmentDTOPage);
    }
}