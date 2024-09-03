package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.DisciplineDTO;
import com.agenda_aulas_api.service.DisciplineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/discipline")
@RequiredArgsConstructor
public class DisciplineController {

    @Autowired
    private final DisciplineService disciplineService;

    @GetMapping
    public ResponseEntity<List<DisciplineDTO>> getAllDiscipline() {
        List<DisciplineDTO> disciplineDTOList = disciplineService.findAll();
        return ResponseEntity.ok(disciplineDTOList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DisciplineDTO> getDisciplineById(@PathVariable UUID id) {
        DisciplineDTO disciplineDTO = disciplineService.findById(id);
        return ResponseEntity.ok(disciplineDTO);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<DisciplineDTO>> findPageDiscipline(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<DisciplineDTO> disciplineDTOPage = disciplineService.findPageDiscipline(
                page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(disciplineDTOPage);
    }

    @PostMapping
    public ResponseEntity<DisciplineDTO> createDiscipline(@Valid @RequestBody DisciplineDTO disciplineDTO) {
        DisciplineDTO savedDisciplineDTO = disciplineService.createDiscipline(disciplineDTO);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDisciplineDTO.getIdDiscipline())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<DisciplineDTO> updateDiscipline(@RequestBody DisciplineDTO objDto, @PathVariable UUID id) {
        DisciplineDTO newObj = disciplineService.updateDiscipline(objDto, id);
        return ResponseEntity.ok().body(newObj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteDiscipline(@PathVariable UUID id) {
        disciplineService.deleteDiscipline(id);
        return ResponseEntity.noContent().build();
    }
}
