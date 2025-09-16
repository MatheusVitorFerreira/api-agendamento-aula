package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.LessonDTO;
import com.agenda_aulas_api.dto.record.LessonRequestRecordDTO;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final DisciplineRepository disciplineRepository;

    public List<Map<String, Object>> findAll() {
        try {
            return lessonRepository.findAll().stream()
                    .map(lesson -> {
                        Map<String, Object> filteredMap = new HashMap<>();
                        filteredMap.put("idLesson", lesson.getIdLesson());
                        filteredMap.put("scheduleClassId", lesson.getScheduleClass() != null
                                ? lesson.getScheduleClass().getIdClassSchedule() : "Não Agendada");
                        filteredMap.put("availableSlots", lesson.getAvailableSlots());
                        return filteredMap;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public LessonDTO findById(UUID idLesson) {
        try {
            Lesson lesson = lessonRepository.findById(idLesson)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + idLesson));
            return LessonDTO.fromLesson(lesson);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public Page<LessonRequestRecordDTO> findPageLesson(
            Integer page,
            Integer linesPerPage,
            String orderBy,
            String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
            return lessonRepository.findAll(pageRequest).map(LessonRequestRecordDTO::fromLesson);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public LessonRequestRecordDTO createLesson(LessonRequestRecordDTO lessonRequestRecordDTO) {
        try {
            Lesson lesson = lessonRequestRecordDTO.toLesson();
            Teacher teacher = teacherRepository.findById(lessonRequestRecordDTO.teacherId())
                    .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: "
                            + lessonRequestRecordDTO.teacherId()));

            Discipline discipline = disciplineRepository.findById(lessonRequestRecordDTO.disciplineId())
                    .orElseThrow(() -> new DisciplineNotFoundException("Discipline not found with id: "
                            + lessonRequestRecordDTO.disciplineId()));

            if (!teacher.getDisciplines().contains(discipline)) {
                throw new InvalidTeachingAssignmentException("The teacher cannot teach the specified discipline.");
            }
            lesson.setTeacher(teacher);
            lesson.setDiscipline(discipline);

            Lesson savedLesson = lessonRepository.save(lesson);

            return LessonRequestRecordDTO.fromLesson(savedLesson);

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to create Lesson: " + e.getMessage());
        }
    }


    @Transactional
    public LessonDTO updateLesson(LessonDTO lessonDTO, UUID idLesson) {
        try {
            Lesson existingLesson = lessonRepository.findById(idLesson)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + idLesson));

            Teacher teacher = teacherRepository.findById(lessonDTO.getTeacherId())
                    .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: "
                            + lessonDTO.getTeacherId()));
            existingLesson.setTeacher(teacher);

            Discipline discipline = disciplineRepository.findById(lessonDTO.getDisciplineId())
                    .orElseThrow(() -> new DisciplineNotFoundException("Discipline not found with id: "
                            + lessonDTO.getDisciplineId()));
            existingLesson.setDiscipline(discipline);

            existingLesson.setAvailableSlots(lessonDTO.getAvailableSlots());
            existingLesson.setLocation(lessonDTO.getLocation());

            Lesson updatedLesson = lessonRepository.save(existingLesson);
            return LessonDTO.fromLesson(updatedLesson);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to update Lesson in the database: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteLesson(UUID lessonId) {
        try {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + lessonId));
            lessonRepository.delete(lesson);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to delete Lesson: " + e.getMessage());
        }
    }
    public List<Map<String, Object>> findLessonsWithoutSchedule() {
        try {
            return lessonRepository.findByScheduleClassIsNull().stream()
                    .map(lesson -> {
                        Map<String, Object> filteredMap = new HashMap<>();
                        filteredMap.put("idLesson", lesson.getIdLesson());
                        filteredMap.put("availableSlots", lesson.getAvailableSlots());
                        filteredMap.put("location", lesson.getLocation());
                        return filteredMap;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }
}
