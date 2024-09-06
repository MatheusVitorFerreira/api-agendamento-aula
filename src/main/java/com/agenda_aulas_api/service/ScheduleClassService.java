package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.LessonDTO;
import com.agenda_aulas_api.dto.ScheduleClassDTO;
import com.agenda_aulas_api.dto.record.ScheduleRequestRecord;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleClassService {
    private final ScheduleClassRepository scheduleClassRepository;

    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentService enrollmentService;
    private final EnrollmentRepository enrollmentRepository;
    private final ScheduleClassStudentRepository scheduleClassStudentRepository;
    private final ScheduleClassTeacherRepository scheduleClassTeacherRepository;

    public List<ScheduleRequestRecord> findAll() {
        try {
            List<ScheduleClass> scheduleClasses = scheduleClassRepository.findAll();
            return scheduleClasses.stream()
                    .map(ScheduleRequestRecord::fromScheduleClass)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException(
                    "Failed to access the database: " + e.getMessage());
        }
    }

    public Page<ScheduleRequestRecord> findPageScheduleClass(
            Integer page,
            Integer linesPerPage,
            String orderBy,
            String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), orderBy);
        Pageable pageable = PageRequest.of(page, linesPerPage, sort);
        Page<ScheduleClass> scheduleClassPage = scheduleClassRepository.findAll(pageable);

        return scheduleClassPage.map(scheduleClass -> {
            LessonDTO lessonDTO = LessonDTO.fromLesson(scheduleClass.getLesson());
            return new ScheduleRequestRecord(
                    scheduleClass.getIdClassSchedule(),
                    scheduleClass.getWeekDays(),
                    lessonDTO,
                    scheduleClass.getStartTime().toString(),
                    scheduleClass.getEndTime().toString()
            );
        });
    }

    public ScheduleRequestRecord findById(UUID idScheduleClass) {
        try {
            ScheduleClass scheduleClass = scheduleClassRepository.findById(idScheduleClass)
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException(
                            "ScheduleClass not found with id: " + idScheduleClass));
            LessonDTO lessonDTO = null;

            if (scheduleClass.getLesson() != null) {
                Lesson lesson = scheduleClass.getLesson();
                lessonDTO = new LessonDTO(
                        lesson.getIdLesson(),
                        lesson.getTeacher() != null ? lesson.getTeacher().getTeacherId() : null,
                        lesson.getDiscipline() != null ? lesson.getDiscipline().getIdDiscipline() : null,
                        lesson.getAvailableSlots(),
                        lesson.getStatus(),
                        lesson.getLocation(),
                        lesson.getStudents() != null ?
                                lesson.getStudents().stream()
                                        .map(Student::getStudentId)
                                        .collect(Collectors.toList()) : List.of(),
                        lesson.getScheduleClass() != null ? lesson.getScheduleClass().getIdClassSchedule() : null,
                        lesson.getClassShift()
                );
            }

            return new ScheduleRequestRecord(
                    scheduleClass.getIdClassSchedule(),
                    scheduleClass.getWeekDays(),
                    lessonDTO,
                    scheduleClass.getStartTime().toString(),
                    scheduleClass.getEndTime().toString()
            );

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException(
                    "Failed to access the database: " + e.getMessage()
            );
        }
    }

    @Transactional
    public ScheduleClassDTO createScheduleClass(ScheduleClassDTO scheduleClassDTO) {
        try {
            ScheduleClass scheduleClass = scheduleClassDTO.toScheduleClass();

            if (scheduleClassDTO.getLessonId() == null) {
                throw new IllegalArgumentException("Lesson ID must be provided to schedule a class.");
            }

            Lesson lesson = lessonRepository.findById(scheduleClassDTO.getLessonId())
                    .orElseThrow(() -> new LessonNotFoundException(
                            "Lesson not found with id: " + scheduleClassDTO.getLessonId()
                    ));
            lesson.setStatus(StatusClass.CONFIRMED);
            List<UUID> studentIds = lesson.getStudents().stream()
                    .map(Student::getStudentId)
                    .toList();
            Set<UUID> uniqueStudentIds = new HashSet<>(studentIds);
            if (uniqueStudentIds.size() < studentIds.size()) {
                throw new DuplicateEntityException("Duplicate student IDs are not allowed.");
            }

            Teacher teacher = lesson.getTeacher();
            if (teacher != null) {
                for (DayOfWeek dayOfWeek : scheduleClassDTO.getWeekDays()) {
                    List<ScheduleClass> conflictingClasses = scheduleClassRepository.findConflictingClasses(
                            teacher, dayOfWeek, scheduleClassDTO.getStartTime(), scheduleClassDTO.getEndTime()
                    );
                    if (!conflictingClasses.isEmpty()) {
                        throw new ScheduleConflictException(
                                "Conflict with existing teacher schedule on " + dayOfWeek + ".");
                    }
                }
            }

            scheduleClass.setClassShift(lesson.getClassShift());
            scheduleClass.setTeacher(lesson.getTeacher());

            ScheduleClass savedScheduleClass = scheduleClassRepository.save(scheduleClass);

            ScheduleClassTeacher scheduleClassTeacher = new ScheduleClassTeacher();
            scheduleClassTeacher.setDaysOfWeek(scheduleClassDTO.getWeekDays());
            scheduleClassTeacher.setStartTime(scheduleClassDTO.getStartTime());
            scheduleClassTeacher.setEndTime(scheduleClassDTO.getEndTime());
            scheduleClassTeacher.setLesson(lesson);
            scheduleClassTeacher.setScheduleClass(savedScheduleClass);
            scheduleClassTeacher.setTeacher(lesson.getTeacher());

            scheduleClassTeacherRepository.save(scheduleClassTeacher);
            lesson.setScheduleClass(savedScheduleClass);
            lessonRepository.save(lesson);
            savedScheduleClass.getScheduleClassTeachers().add(scheduleClassTeacher);
            scheduleClassRepository.save(savedScheduleClass);
            return ScheduleClassDTO.fromScheduleClass(savedScheduleClass);

        } catch (LessonNotFoundException | DuplicateEntityException | ScheduleConflictException e) {
            throw new IllegalStateException("Failed to schedule class: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public ScheduleClassDTO updateScheduleClass(ScheduleClassDTO scheduleClassDTO, UUID idClass) {
        try {
            ScheduleClass existingSchedule = scheduleClassRepository.findById(idClass)
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException(
                            "Schedule Class not found with id: " + idClass));

            existingSchedule.setWeekDays(scheduleClassDTO.getWeekDays());

            if (scheduleClassDTO.getLessonId() != null) {
                Lesson lesson = lessonRepository.findById(scheduleClassDTO.getLessonId())
                        .orElseThrow(() -> new LessonNotFoundException(
                                "Lesson not found with id: " + scheduleClassDTO.getLessonId()));

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
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException(
                            "ScheduleClass not found with id: " + idScheduleClass));
            Lesson lesson = scheduleClass.getLesson();
            if (lesson != null) {
                lesson.getStudents().clear();
                lessonRepository.delete(lesson);
                scheduleClass.setLesson(null);
            }

            scheduleClassStudentRepository.deleteByScheduleClassId(idScheduleClass);

            scheduleClassTeacherRepository.deleteByScheduleClassId(idScheduleClass);

            scheduleClassRepository.delete(scheduleClass);

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to delete ScheduleClass: " + e.getMessage());
        }
    }

    public List<ScheduleRequestRecord> getAvailableClassesForEnrollment() {
        try {
            List<ScheduleClass> allClasses = scheduleClassRepository.findAll();
            return allClasses.stream()
                    .filter(scheduleClass -> scheduleClass.getLesson() != null &&
                            scheduleClass.getLesson().getAvailableSlots() > 0)
                    .map(ScheduleRequestRecord::fromScheduleClass)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException(
                    "Failed to access the database: " + e.getMessage()
            );
        }
    }

    @Transactional
    public void addStudentToScheduleClass(UUID scheduleClassId, UUID studentId) {
        ScheduleClass scheduleClass = scheduleClassRepository.findById(scheduleClassId)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException(
                        "Classe de agendamento não encontrada"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado"));

        Lesson lesson = scheduleClass.getLesson();
        if (lesson == null) {
            throw new LessonNotFoundException("Lição não encontrada para a classe de agendamento.");
        }

        if (lesson.getAvailableSlots() <= 0) {
            throw new NoAvailableSlotsException("Não há slots disponíveis para esta classe de agendamento.");
        }

        List<DayOfWeek> weekDays = scheduleClass.getWeekDays();

        List<ScheduleClassStudent> existingScheduleClassStudents = scheduleClass.getScheduleClassStudents();
        Set<DayOfWeek> registeredDays = new HashSet<>();
        for (ScheduleClassStudent scs : existingScheduleClassStudents) {
            if (scs.getStudent().getStudentId().equals(studentId)) {
                registeredDays.add(scs.getDayOfWeek());
            }
        }

        List<ScheduleClassStudent> newScheduleClassStudents = new ArrayList<>();
        for (DayOfWeek dayOfWeek : weekDays) {
            if (!registeredDays.contains(dayOfWeek)) {
                ScheduleClassStudent scheduleClassStudent = new ScheduleClassStudent();
                scheduleClassStudent.setScheduleClass(scheduleClass);
                scheduleClassStudent.setStudent(student);
                scheduleClassStudent.setDayOfWeek(dayOfWeek);
                scheduleClassStudent.setStartTime(scheduleClass.getStartTime());
                scheduleClassStudent.setEndTime(scheduleClass.getEndTime());

                newScheduleClassStudents.add(scheduleClassStudent);
            }
        }
        if (!newScheduleClassStudents.isEmpty()) {
            scheduleClassStudentRepository.saveAll(newScheduleClassStudents);
            scheduleClass.getScheduleClassStudents().addAll(newScheduleClassStudents);

            lesson.setAvailableSlots(lesson.getAvailableSlots() - 1);
            lessonRepository.save(lesson);

            scheduleClassRepository.save(scheduleClass);
            lesson.getStudents().removeIf(existingStudent -> existingStudent.equals(student));
            lesson.addStudent(student);
            lessonRepository.save(lesson);
        }

        Enrollment enrollment = enrollmentRepository.findByStudentAndScheduleClass(student, scheduleClass)
                .orElse(new Enrollment(student, scheduleClass, StatusClass.CONFIRMED));
        enrollment.setStatus(StatusClass.CONFIRMED);
        enrollmentService.saveEnrollment(enrollment);
    }
    @Transactional
    public void removeStudentFromScheduleClass(UUID scheduleClassId, UUID studentId) {
        ScheduleClass scheduleClass = scheduleClassRepository.findById(scheduleClassId)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException(
                        "Classe de agendamento não encontrada"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado"));

        Lesson lesson = scheduleClass.getLesson();
        if (lesson == null) {
            throw new LessonNotFoundException("Lição não encontrada para a classe de agendamento.");
        }

        List<ScheduleClassStudent> scheduleClassStudents = scheduleClass.getScheduleClassStudents();
        ScheduleClassStudent toRemove = null;

        for (ScheduleClassStudent scs : scheduleClassStudents) {
            if (scs.getStudent().getStudentId().equals(studentId)) {
                toRemove = scs;
                break;
            }
        }

        if (toRemove != null) {
            scheduleClassStudentRepository.delete(toRemove);
            scheduleClass.getScheduleClassStudents().remove(toRemove);

            lesson.setAvailableSlots(lesson.getAvailableSlots() + 1);
            lessonRepository.save(lesson);

            lesson.removeStudent(student);
            lessonRepository.save(lesson);

            Enrollment enrollment = enrollmentRepository.findByStudentAndScheduleClass(student, scheduleClass)
                    .orElseThrow(() -> new EnrollmentNotFoundException("Matrícula não encontrada"));
            enrollmentRepository.delete(enrollment);
        } else {
            throw new ScheduleClassRepositoryNotFoundException(
                    "Associação de aluno não encontrada para a classe de agendamento.");
        }
    }
}
