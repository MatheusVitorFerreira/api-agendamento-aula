package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.ScheduleDTO;
import com.agenda_aulas_api.dto.record.ScheduleRequestRecord;
import com.agenda_aulas_api.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleRequestRecord>> findAll() {
        List<ScheduleRequestRecord> list = scheduleService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleRequestRecord> findById(@PathVariable UUID id) {
        ScheduleRequestRecord dto = scheduleService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ScheduleRequestRecord>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "orderBy", defaultValue = "date") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<ScheduleRequestRecord> pageDto = scheduleService.findPage(page, size, orderBy, direction);
        return ResponseEntity.ok().body(pageDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable UUID id, @RequestBody ScheduleDTO dto) {
        ScheduleDTO updatedDto = scheduleService.updateSchedule(id, dto);
        return ResponseEntity.ok().body(updatedDto);
    }

    @PostMapping
    public ResponseEntity<ScheduleDTO> create(@RequestBody ScheduleDTO dto) {
        ScheduleDTO created = scheduleService.createSchedule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
