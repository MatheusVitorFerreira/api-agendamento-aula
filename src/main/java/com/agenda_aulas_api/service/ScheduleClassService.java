package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.dto.ScheduleClassDTO;
import com.agenda_aulas_api.exception.erros.DatabaseNegatedAccessException;
import com.agenda_aulas_api.exception.erros.LessonNotFoundException;
import com.agenda_aulas_api.exception.erros.ScheduleClassRepositoryNotFoundException;
import com.agenda_aulas_api.repository.LessonRepository;
import com.agenda_aulas_api.repository.ScheduleClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleClassService {

    private final ScheduleClassRepository scheduleClassRepository;
    private final LessonRepository lessonRepository;

    public List<ScheduleClassDTO> findAll() {
        try {
            List<ScheduleClass> scheduleClasses = scheduleClassRepository.findAll();
            return scheduleClasses.stream()
                    .map(ScheduleClassDTO::fromScheduleClass)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public ScheduleClassDTO findById(UUID idScheduleClass) {
        try {
            ScheduleClass scheduleClass = scheduleClassRepository.findById(idScheduleClass)
                    .orElseThrow(() ->
                            new ScheduleClassRepositoryNotFoundException("ScheduleClass not found with id: " + idScheduleClass));
            return ScheduleClassDTO.fromScheduleClass(scheduleClass);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public ScheduleClassDTO createOrUpdateScheduleClass(ScheduleClassDTO scheduleClassDTO) {
        try {
            ScheduleClass scheduleClass;
            if (scheduleClassDTO.getIdClass() != null) {
                scheduleClass = scheduleClassRepository.findById(scheduleClassDTO.getIdClass())
                        .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("ScheduleClass not found with id: " + scheduleClassDTO.getIdClass()));
            } else {
                scheduleClass = new ScheduleClass();
            }

            if (scheduleClassDTO.getLessonIds() != null) {
                List<Lesson> lessons = lessonRepository.findAllById(scheduleClassDTO.getLessonIds());
                if (lessons.size() != scheduleClassDTO.getLessonIds().size()) {
                    throw new LessonNotFoundException("One or more Lessons not found");
                }
                scheduleClass.setLessons(lessons);
            } else {
                scheduleClass.setLessons(null);
            }

            scheduleClass.setWeekDays(scheduleClassDTO.getWeekDays());
            scheduleClass.setLocation(scheduleClassDTO.getLocation());

            ScheduleClass savedScheduleClass = scheduleClassRepository.save(scheduleClass);
            return ScheduleClassDTO.fromScheduleClass(savedScheduleClass);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create or update ScheduleClass: " + e.getMessage(), e);
        }
    }
}
