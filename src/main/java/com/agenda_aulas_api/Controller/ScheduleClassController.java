package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.ScheduleClassDTO;
import com.agenda_aulas_api.dto.record.ScheduleRecord;
import com.agenda_aulas_api.service.ScheduleClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/schedule-classes")
@RequiredArgsConstructor
public class ScheduleClassController {

    private final ScheduleClassService scheduleClassService;

    @GetMapping
    public ResponseEntity<List<ScheduleClassDTO>> findAll() {
        List<ScheduleClassDTO> scheduleClasses = scheduleClassService.findAll();
        return ResponseEntity.ok(scheduleClasses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleRecord> findById(@PathVariable UUID id) {
        ScheduleRecord scheduleRecord = scheduleClassService.findById(id);
        return ResponseEntity.ok(scheduleRecord);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ScheduleRecord>> findPageScheduleClass(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "idClass") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<ScheduleRecord> scheduleRecords = scheduleClassService.findPageScheduleClass(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(scheduleRecords);
    }

    @PostMapping
    public ResponseEntity<ScheduleClassDTO> createScheduleClass(@RequestBody ScheduleClassDTO scheduleClassDTO) {
        ScheduleClassDTO createdScheduleClass = scheduleClassService.createScheduleClass(scheduleClassDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdScheduleClass);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleClassDTO> updateScheduleClass(@RequestBody ScheduleClassDTO scheduleClassDTO, @PathVariable UUID id) {
        ScheduleClassDTO updatedScheduleClass = scheduleClassService.updateScheduleClass(scheduleClassDTO, id);
        return ResponseEntity.ok(updatedScheduleClass);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleClass(@PathVariable UUID id) {
        scheduleClassService.deleteScheduleClass(id);
        return ResponseEntity.noContent().build();
    }
}
