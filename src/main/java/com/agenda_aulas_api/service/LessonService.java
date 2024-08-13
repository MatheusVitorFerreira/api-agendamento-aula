package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Discipline;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.Teacher;
import com.agenda_aulas_api.dto.LessonDTO;
import com.agenda_aulas_api.dto.record.LessonRecord;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.agenda_aulas_api.repository.DisciplineRepository;
import com.agenda_aulas_api.repository.LessonRepository;
import com.agenda_aulas_api.repository.TeacherRepository;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final DisciplineRepository disciplineRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    public List<Map<String, Object>> findAll() {
        try {
            return lessonRepository.findAll().stream()
                    .map(lesson -> {
                        Map<String, Object> filteredMap = new HashMap<>();
                        filteredMap.put("idLesson", lesson.getIdLesson());
                        filteredMap.put("scheduleClassId", lesson.getScheduleClass()
                                != null ? lesson.getScheduleClass().getIdClass() : "Não Agendada");
                        filteredMap.put("startTime", lesson.getStartTime());
                        filteredMap.put("endTime", lesson.getEndTime());
                        filteredMap.put("availableSlots", lesson.getAvailableSlots());
                        return filteredMap;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> findLessonsWithoutAvailableSlots() {
        try {
            return lessonRepository.findAll().stream()
                    .filter(lesson -> lesson.getAvailableSlots() == 0)
                    .map(lesson -> {
                        Map<String, Object> filteredMap = new HashMap<>();
                        filteredMap.put("idLesson", lesson.getIdLesson());
                        filteredMap.put("scheduleClassId", lesson.getScheduleClass()
                                != null ? lesson.getScheduleClass().getIdClass() : "Não Agendada");
                        filteredMap.put("startTime", lesson.getStartTime());
                        filteredMap.put("endTime", lesson.getEndTime());
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

    public LessonDTO createLesson(LessonDTO lessonDTO) {
        try {
            Lesson lesson = lessonDTO.toLesson();

            if (lessonDTO.getTeacherId() != null) {
                Teacher teacher = teacherRepository.findById(lessonDTO.getTeacherId())
                        .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + lessonDTO.getTeacherId()));
                lesson.setTeacher(teacher);
            }

            if (lessonDTO.getDisciplineId() != null) {
                Discipline discipline = disciplineRepository.findById(lessonDTO.getDisciplineId())
                        .orElseThrow(() -> new DisciplineNotFoundException("Discipline not found with id: " + lessonDTO.getDisciplineId()));
                lesson.setDiscipline(discipline);
            }

            Lesson savedLesson = lessonRepository.save(lesson);
            return LessonDTO.fromLesson(savedLesson);

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to create Lesson: " + e.getMessage());
        }
    }

    @Transactional
    public LessonDTO updateLesson(LessonDTO lessonDTO, UUID idLesson) {
        try {
            Lesson existingLesson = lessonRepository.findById(idLesson)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + idLesson));

            if (lessonDTO.getTeacherId() != null) {
                Teacher teacher = teacherRepository.findById(lessonDTO.getTeacherId())
                        .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + lessonDTO.getTeacherId()));
                existingLesson.setTeacher(teacher);
            }

            if (lessonDTO.getDisciplineId() != null) {
                Discipline discipline = disciplineRepository.findById(lessonDTO.getDisciplineId())
                        .orElseThrow(() -> new DisciplineNotFoundException("Discipline not found with id: " + lessonDTO.getDisciplineId()));
                existingLesson.setDiscipline(discipline);
            }

            existingLesson.setAvailableSlots(lessonDTO.getAvailableSlots());
            existingLesson.setLocation(lessonDTO.getLocation());
            existingLesson.setEndTime(lessonDTO.toLesson().getEndTime());
            existingLesson.setStartTime(lessonDTO.toLesson().getStartTime());

            Lesson updatedLesson = lessonRepository.save(existingLesson);
            return LessonDTO.fromLesson(updatedLesson);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to update Lesson in the database: " + e.getMessage());
        }
    }

    public void deleteLesson(UUID idLesson) {
        Lesson lesson = lessonRepository.findById(idLesson).orElseThrow(() ->
                new LessonNotFoundException("Lesson not found with id: " + idLesson));
        lessonRepository.deleteById(idLesson);
    }

    @Transactional
    public void addStudentToLesson(UUID lessonId, LessonRecord lessonRecord) {
        UUID studentId = lessonRecord.idStudent();

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + lessonId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

        if (lesson.getAvailableSlots() > 0) {
            lesson.addStudent(student);
            lesson.setAvailableSlots(lesson.getAvailableSlots() - 1);
            lessonRepository.save(lesson);
            if (!student.getLessons().contains(lesson)) {
                student.getLessons().add(lesson);
                studentRepository.save(student);
            }
        } else {
            throw new NoAvailableSlotsException("No available slots for this lesson.");
        }
    }
}
