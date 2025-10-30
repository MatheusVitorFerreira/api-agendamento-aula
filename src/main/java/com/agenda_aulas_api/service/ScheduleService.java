package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.ScheduleDTO;
import com.agenda_aulas_api.dto.record.ScheduleRequestRecord;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.LessonRepository;
import com.agenda_aulas_api.repository.ScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final LessonRepository lessonRepository;

    public List<ScheduleRequestRecord> findAll() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleRequestRecord::fromSchedule)
                .collect(Collectors.toList());
    }

    public Page<ScheduleRequestRecord> findPage(Integer page, Integer size, String orderBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), orderBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Schedule> schedules = scheduleRepository.findAll(pageable);
        return schedules.map(ScheduleRequestRecord::fromSchedule);
    }

    public ScheduleRequestRecord findById(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("Schedule not found with id: " + scheduleId));
        return ScheduleRequestRecord.fromSchedule(schedule);
    }

    @Transactional
    public ScheduleDTO createSchedule(ScheduleDTO dto) {
        if (dto.getLessonId() == null)
            throw new IllegalArgumentException("Lesson ID must be provided.");

        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + dto.getLessonId()));
        checkScheduleConflict(
                lesson.getTeacher(),
                dto.getDate(),
                dto.getStartTime(),
                dto.getEndTime(),
                null
        );
        Schedule schedule = dto.toSchedule(lesson);
        Schedule saved = scheduleRepository.save(schedule);
        lesson.setSchedule(saved);
        lessonRepository.save(lesson);
        return ScheduleDTO.fromSchedule(saved);
    }

    @Transactional
    public ScheduleDTO updateSchedule(UUID scheduleId, ScheduleDTO dto) {
        Schedule existing = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("Schedule not found with id: " + scheduleId));
        Lesson lessonToUpdate;
        if (dto.getLessonId() != null && (existing.getLesson() == null || !existing.getLesson().getIdLesson().equals(dto.getLessonId()))) {
            lessonToUpdate = lessonRepository.findById(dto.getLessonId())
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + dto.getLessonId()));
        } else {
            lessonToUpdate = existing.getLesson();
        }
        if (lessonToUpdate != null) {
            checkScheduleConflict(
                    lessonToUpdate.getTeacher(),
                    dto.getDate(),
                    dto.getStartTime(),
                    dto.getEndTime(),
                    scheduleId
            );
        }

        existing.setDate(dto.getDate());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setShift(dto.getShift());

        if (lessonToUpdate != existing.getLesson()) {
            existing.setLesson(lessonToUpdate);
            if (lessonToUpdate != null) {
                lessonToUpdate.setSchedule(existing);
                lessonRepository.save(lessonToUpdate);
            }
        }

        Schedule updated = scheduleRepository.save(existing);
        return ScheduleDTO.fromSchedule(updated);
    }

    @Transactional
    public void deleteSchedule(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("Schedule not found"));
        Lesson lesson = schedule.getLesson();
        if (lesson != null) {
            lesson.setSchedule(null);
            lessonRepository.save(lesson);
        }
        scheduleRepository.delete(schedule);
    }
    private void checkScheduleConflict(Teacher teacher, LocalDate date, LocalTime startTime, LocalTime endTime, UUID scheduleIdToIgnore) {
        if (teacher == null) return;
        List<Schedule> conflicts = scheduleRepository.findOverlappingSchedules(
                teacher.getTeacherId(),
                date,
                startTime,
                endTime
        );
        boolean realConflict = conflicts.stream()
                .anyMatch(s -> !s.getIdSchedule().equals(scheduleIdToIgnore));
        if (realConflict) {
            throw new ScheduleConflictException(
                    "Conflito de agendamento: O professor já tem uma aula no dia " + date +
                            " que conflita com o horário " + startTime + " - " + endTime);
        }
    }
}