package com.agenda_aulas_api.Controller;

import com.agenda_aulas_api.dto.ScheduleClassDTO;
import com.agenda_aulas_api.dto.record.LessonRecord;
import com.agenda_aulas_api.dto.record.ScheduleRequestRecord;
import com.agenda_aulas_api.service.ScheduleClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sistema-agendamento-aula/api/v1/schedule-classes")
@RequiredArgsConstructor
public class ScheduleClassController {

    private final ScheduleClassService scheduleClassService;

    @GetMapping
    public ResponseEntity<List<ScheduleRequestRecord>> findAll() {
        List<ScheduleRequestRecord> scheduleClasses = scheduleClassService.findAll();
        return ResponseEntity.ok(scheduleClasses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleRequestRecord> findById(@PathVariable UUID id) {
        ScheduleRequestRecord scheduleRequestRecord = scheduleClassService.findById(id);
        return ResponseEntity.ok(scheduleRequestRecord);
    }

    @GetMapping("/available-classes")
    public ResponseEntity<List<ScheduleRequestRecord>> getAvailableClassesForEnrollment() {
        List<ScheduleRequestRecord> scheduleRequestRecord = scheduleClassService.getAvailableClassesForEnrollment();
        return ResponseEntity.ok(scheduleRequestRecord);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ScheduleRequestRecord>> findPageScheduleClass(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "idClassSchedule") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<ScheduleRequestRecord> scheduleRecords =
                scheduleClassService.findPageScheduleClass(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(scheduleRecords);
    }


    @PostMapping()
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ScheduleClassDTO> createScheduleClass(@RequestBody ScheduleClassDTO scheduleClassDTO) {
        ScheduleClassDTO createdScheduleClass = scheduleClassService.createScheduleClass(scheduleClassDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdScheduleClass);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ScheduleClassDTO> updateScheduleClass(
            @RequestBody ScheduleClassDTO scheduleClassDTO,
            @PathVariable UUID id) {
        ScheduleClassDTO updatedScheduleClass = scheduleClassService.updateScheduleClass(scheduleClassDTO, id);
        return ResponseEntity.ok(updatedScheduleClass);
    }

    @DeleteMapping("/{scheduleClassId}/students/{studentId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<String> removeStudentFromScheduleClass(
            @PathVariable UUID scheduleClassId,
            @PathVariable UUID studentId) {
        try {
            scheduleClassService.removeStudentFromScheduleClass(scheduleClassId, studentId);
            return ResponseEntity.ok("Aluno removido com sucesso da classe de agendamento.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao remover aluno: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteScheduleClass(@PathVariable UUID id) {
        scheduleClassService.deleteScheduleClass(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{scheduleClassId}/students")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MODERATOR')")
    public ResponseEntity<String> addStudentToScheduleClass(
            @PathVariable UUID scheduleClassId,
            @RequestBody @Valid LessonRecord lessonRecord) {
            scheduleClassService.addStudentToScheduleClass(scheduleClassId, lessonRecord.studentId());
            return ResponseEntity.status(HttpStatus.OK).body("Aluno adicionado com sucesso.");
    }
}
