package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.LessonDTO;
import com.agenda_aulas_api.dto.ScheduleClassDTO;
import com.agenda_aulas_api.dto.record.LessonRecord;
import com.agenda_aulas_api.dto.record.ScheduleRequestRecord;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleClassService {

    private final ScheduleClassRepository scheduleClassRepository;
    private final LessonRepository lessonRepository;
    private final ScheduleClassTeacherRepository scheduleClassTeacherRepository;
    private final TimeTableRepository timeTableRepository;
    private final StudentRepository studentRepository;

    public List<ScheduleRequestRecord> findAll() {
        try {
            List<ScheduleClass> scheduleClasses = scheduleClassRepository.findAll();
            return scheduleClasses.stream().map(ScheduleRequestRecord::fromScheduleClass).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public Page<ScheduleRequestRecord> findPageScheduleClass(Integer page, Integer linesPerPage, String orderBy, String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
            return scheduleClassRepository.findAll(pageRequest).map(ScheduleRequestRecord::fromScheduleClass);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public ScheduleRequestRecord findById(UUID idScheduleClass) {
        try {
            ScheduleClass scheduleClass = scheduleClassRepository.findById(idScheduleClass)
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("ScheduleClass not found with id: " + idScheduleClass));

            LessonDTO lessonDTO = null;

            if (scheduleClass.getLesson() != null) {
                Lesson lesson = scheduleClass.getLesson();

                lessonDTO = new LessonDTO(
                        lesson.getIdLesson(),
                        lesson.getTeacher() != null ? lesson.getTeacher().getIdTeacher() : null,
                        lesson.getDiscipline() != null ? lesson.getDiscipline().getIdDiscipline() : null,
                        lesson.getAvailableSlots(),
                        lesson.getStatus(),
                        lesson.getLocation(),
                        lesson.getStudents() != null ? lesson.getStudents().stream().map(Student::getIdStudent).collect(Collectors.toList()) : List.of(),
                        lesson.getScheduleClass() != null ? lesson.getScheduleClass().getIdClassSchedule() : null
                );
            }

            return new ScheduleRequestRecord(
                    scheduleClass.getIdClassSchedule(),
                    scheduleClass.getWeekDays(),
                    lessonDTO,
                    scheduleClass.getStartTime().toString(),
                    scheduleClass.getEndTime().toString()
            );

        } catch (ScheduleClassRepositoryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public ScheduleClassDTO createScheduleClass(ScheduleClassDTO scheduleClassDTO) {
        try {
            // Converter DTO para a entidade ScheduleClass
            ScheduleClass scheduleClass = scheduleClassDTO.toScheduleClass();

            // Verificar se Lesson ID foi fornecido
            if (scheduleClassDTO.getLessonId() == null) {
                throw new IllegalArgumentException("Lesson ID must be provided to schedule a class.");
            }

            // Buscar a lição associada
            Lesson lesson = lessonRepository.findById(scheduleClassDTO.getLessonId())
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + scheduleClassDTO.getLessonId()));

            // Associar a lição ao ScheduleClass
            scheduleClass.setLesson(lesson);

            // Verificar conflitos para cada dia da semana
            for (DayOfWeek dayOfWeek : scheduleClass.getWeekDays()) {
                // Verificar conflitos com o horário do professor
                List<ScheduleClassTeacher> conflictingTeachers = scheduleClassTeacherRepository.findByTeacherAndDayOfWeekAndTimeRange(
                        lesson.getTeacher(), dayOfWeek, scheduleClassDTO.getStartTime(), scheduleClassDTO.getEndTime());

                if (!conflictingTeachers.isEmpty()) {
                    throw new IllegalStateException("Conflict with existing teacher schedule on " + dayOfWeek + ".");
                }

                // Verificar conflitos com o horário das lições
                List<TimeTable> conflictingLessons = timeTableRepository.findByLessonAndDayOfWeekAndTimeRange(
                        lesson, dayOfWeek, scheduleClassDTO.getStartTime(), scheduleClassDTO.getEndTime());

                if (!conflictingLessons.isEmpty()) {
                    throw new IllegalStateException("Conflict with existing lesson schedule on " + dayOfWeek + ".");
                }
            }

            // Salvar ScheduleClass
            ScheduleClass savedScheduleClass = scheduleClassRepository.save(scheduleClass);

            // Atualizar lição com o ScheduleClass salvo
            lesson.setScheduleClass(savedScheduleClass);
            lessonRepository.save(lesson);

            // Criar e salvar ScheduleClassTeacher e TimeTable para cada dia da semana
            for (DayOfWeek dayOfWeek : savedScheduleClass.getWeekDays()) {
                // Criar e salvar ScheduleClassTeacher
                ScheduleClassTeacher scheduleClassTeacher = new ScheduleClassTeacher();
                scheduleClassTeacher.setDaysOfWeek(new ArrayList<>(savedScheduleClass.getWeekDays())); // Define todos os dias da semana
                scheduleClassTeacher.setScheduleClass(savedScheduleClass);
                scheduleClassTeacher.setLesson(lesson);
                scheduleClassTeacher.setStartTime(savedScheduleClass.getStartTime());
                scheduleClassTeacher.setEndTime(savedScheduleClass.getEndTime());
                scheduleClassTeacher.setTeacher(lesson.getTeacher());

                if (scheduleClassTeacher.getTeacher() == null) {
                    throw new IllegalStateException("Cannot schedule class. Lesson does not have an assigned teacher.");
                }

                scheduleClassTeacherRepository.save(scheduleClassTeacher);

                // Criar e salvar TimeTable
                TimeTable timeTable = timeTableRepository.findByLessonIdAndDayOfWeek(lesson.getIdLesson(), dayOfWeek)
                        .orElse(new TimeTable());

                timeTable.setLesson(lesson);
                timeTable.setDayOfWeek(dayOfWeek);
                timeTable.setScheduleClass(savedScheduleClass);
                timeTable.setStartTime(savedScheduleClass.getStartTime());
                timeTable.setEndTime(savedScheduleClass.getEndTime());

                if (timeTable.getStudents() == null || timeTable.getStudents().isEmpty()) {
                    timeTable.setStudents(new ArrayList<>(lesson.getStudents()));
                }

                timeTableRepository.save(timeTable);
            }

            // Retornar o DTO da ScheduleClass criada
            return ScheduleClassDTO.fromScheduleClass(savedScheduleClass);

        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Failed to schedule class: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public void addStudentToScheduleClass(LessonRecord lessonRecord, UUID scheduleClassId) {
        try {
            UUID studentId = lessonRecord.studentId();
            ScheduleClass scheduleClass = scheduleClassRepository.findById(scheduleClassId)
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("ScheduleClass not found with id: " + scheduleClassId));

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

            List<TimeTable> conflictingTimeTables = timeTableRepository.findConflictingTimeTables(
                    student, scheduleClass.getWeekDays(), scheduleClass.getStartTime(), scheduleClass.getEndTime());

            if (!conflictingTimeTables.isEmpty()) {
                throw new IllegalStateException("Student is already enrolled in another class at the same time.");
            }

            Lesson lesson = scheduleClass.getLesson();
            if (lesson == null) {
                throw new IllegalStateException("No lesson associated with this schedule class.");
            }

            System.out.println("Before adding student: " + lesson.getStudents().size());
            lesson.addStudent(student);

            // Persiste a lição com o estudante
            lessonRepository.save(lesson);
            System.out.println("After adding student: " + lesson.getStudents().size());

            // Persistir TimeTable
            for (DayOfWeek dayOfWeek : scheduleClass.getWeekDays()) {
                TimeTable timeTable = new TimeTable();
                timeTable.setLesson(lesson);
                timeTable.setScheduleClass(scheduleClass);
                timeTable.setDayOfWeek(dayOfWeek);
                timeTable.setStartTime(scheduleClass.getStartTime());
                timeTable.setEndTime(scheduleClass.getEndTime());
                timeTable.getStudents().add(student);

                Teacher teacher = lesson.getTeacher();
                if (teacher != null) {
                    timeTable.setTeacher(teacher);
                }

                timeTableRepository.save(timeTable);
            }

        } catch (StudentNotFoundException | ScheduleClassRepositoryNotFoundException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public ScheduleClassDTO updateScheduleClass(ScheduleClassDTO scheduleClassDTO, UUID idClass) {
        try {
            ScheduleClass existingSchedule = scheduleClassRepository.findById(idClass)
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("Schedule Class not found with id: " + idClass));

            existingSchedule.setWeekDays(scheduleClassDTO.getWeekDays());

            if (scheduleClassDTO.getLessonId() != null) {
                Lesson lesson = lessonRepository.findById(scheduleClassDTO.getLessonId())
                        .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + scheduleClassDTO.getLessonId()));

                if (lesson.getAvailableSlots() <= 0) {
                    throw new NoAvailableSlotsException("Cannot update class. Lesson does not have available slots.");
                }

                lesson.setStatus(StatusClass.CONFIRMED);
                lesson.setAvailableSlots(lesson.getAvailableSlots() - 1);
                lessonRepository.save(lesson);

                existingSchedule.setLesson(lesson);
            } else {
                existingSchedule.setLesson(null);
            }

            ScheduleClass updatedScheduleClass = scheduleClassRepository.save(existingSchedule);
            return ScheduleClassDTO.fromScheduleClass(updatedScheduleClass);
        } catch (LessonNotFoundException | NoAvailableSlotsException | IllegalArgumentException e) {
            throw new IllegalStateException("Failed to update schedule class: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to update Lesson in the database: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteScheduleClass(UUID idScheduleClass) {
        try {
            ScheduleClass scheduleClass = scheduleClassRepository.findById(idScheduleClass)
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("ScheduleClass not found with id: " + idScheduleClass));
            Lesson lesson = scheduleClass.getLesson();
            if (lesson != null) {
                lesson.setScheduleClass(null);
                lessonRepository.save(lesson);

                lessonRepository.delete(lesson);
            }

            // Remove ScheduleClass
            scheduleClassRepository.delete(scheduleClass);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to delete ScheduleClass: " + e.getMessage());
        }
    }
}


