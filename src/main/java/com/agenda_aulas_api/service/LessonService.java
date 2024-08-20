package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.LessonDTO;
import com.agenda_aulas_api.dto.record.LessonRecordDTO;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final DisciplineRepository disciplineRepository;
    private final StudentRepository studentRepository;
    private final ScheduleClassStudentRepository scheduleClassStudentRepository;

    public List<Map<String, Object>> findAll() {
        try {
            return lessonRepository.findAll().stream()
                    .map(lesson -> {
                        Map<String, Object> filteredMap = new HashMap<>();
                        filteredMap.put("idLesson", lesson.getIdLesson());
                        filteredMap.put("scheduleClassId", lesson.getScheduleClass() != null
                                ? lesson.getScheduleClass().getIdClassSchedule() : "Não Agendada");
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
                    .filter(lesson -> lesson.getScheduleClass() == null)
                    .map(lesson -> {
                        Map<String, Object> filteredMap = new HashMap<>();
                        filteredMap.put("idLesson", lesson.getIdLesson());
                        filteredMap.put("scheduleClassId", "Não Agendada");
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

    public Page<LessonRecordDTO> findPageLesson(Integer page, Integer linesPerPage, String orderBy, String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
            return lessonRepository.findAll(pageRequest).map(LessonRecordDTO::fromLesson);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public LessonDTO createLesson(LessonDTO lessonDTO) {
        try {
            Lesson lesson = lessonDTO.toLesson();
            Teacher teacher = teacherRepository.findById(lessonDTO.getTeacherId())
                    .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: "
                            + lessonDTO.getTeacherId()));
            lesson.setTeacher(teacher);

            Discipline discipline = disciplineRepository.findById(lessonDTO.getDisciplineId())
                    .orElseThrow(() -> new DisciplineNotFoundException("Discipline not found with id: "
                            + lessonDTO.getDisciplineId()));
            lesson.setDiscipline(discipline);

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
            existingLesson.setStartTime(LocalTime.parse(lessonDTO.getStartTime(), LessonDTO.TIME_FORMATTER));
            existingLesson.setEndTime(LocalTime.parse(lessonDTO.getEndTime(), LessonDTO.TIME_FORMATTER));

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
    @Transactional
    public void addStudentToLesson(UUID lessonId, UUID studentId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + lessonId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

        if (lesson.getAvailableSlots() > 0) {
            // Adiciona aluno à lição
            lesson.addStudent(student);
            lesson.setAvailableSlots(lesson.getAvailableSlots() - 1);
            lessonRepository.save(lesson);

            // Atualiza a relação entre aluno e lição
            if (!student.getLessons().contains(lesson)) {
                student.getLessons().add(lesson);
                studentRepository.save(student);
            }


            ScheduleClassStudent scheduleClassStudent = new ScheduleClassStudent();
            scheduleClassStudent.setLesson(lesson);
            scheduleClassStudent.setStudent(student);
            scheduleClassStudent.setStartTime(lesson.getStartTime());
            scheduleClassStudent.setEndTime(lesson.getEndTime());

            scheduleClassStudentRepository.save(scheduleClassStudent);
        } else {
            throw new NoAvailableSlotsException("No available slots for this lesson.");
        }
    }
}
