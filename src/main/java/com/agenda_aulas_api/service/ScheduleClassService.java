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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleClassService {

    private final ScheduleClassRepository scheduleClassRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentService enrollmentService;
    private final ScheduleStatisticsService scheduleStatisticsService;
    private final EnrollmentRepository enrollmentRepository;
    private final ScheduleClassStudentRepository scheduleClassStudentRepository;
    private final ScheduleClassTeacherRepository scheduleClassTeacherRepository;

    public List<ScheduleRequestRecord> findAll() {
        return scheduleClassRepository.findAll().stream()
                .map(ScheduleRequestRecord::fromScheduleClass)
                .collect(Collectors.toList());
    }

    public Page<ScheduleRequestRecord> findPageScheduleClass(Integer page, Integer linesPerPage, String orderBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), orderBy);
        Pageable pageable = PageRequest.of(page, linesPerPage, sort);
        Page<ScheduleClass> scheduleClassPage = scheduleClassRepository.findAll(pageable);

        return scheduleClassPage.map(sc -> {
            LessonDTO lessonDTO = LessonDTO.fromLesson(sc.getLesson());
            return new ScheduleRequestRecord(
                    sc.getIdClassSchedule(),
                    lessonDTO,
                    sc.getDate() != null ? sc.getDate().toString() : null,
                    sc.getStartTime() != null ? sc.getStartTime().toString() : null,
                    sc.getEndTime() != null ? sc.getEndTime().toString() : null
            );
        });
    }

    public ScheduleRequestRecord findById(UUID idScheduleClass) {
        ScheduleClass sc = scheduleClassRepository.findById(idScheduleClass)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("ScheduleClass not found with id: " + idScheduleClass));

        LessonDTO lessonDTO = sc.getLesson() != null ? LessonDTO.fromLesson(sc.getLesson()) : null;

        return new ScheduleRequestRecord(
                sc.getIdClassSchedule(),
                lessonDTO,
                sc.getDate() != null ? sc.getDate().toString() : null,
                sc.getStartTime() != null ? sc.getStartTime().toString() : null,
                sc.getEndTime() != null ? sc.getEndTime().toString() : null
        );
    }

    @Transactional
    public ScheduleClassDTO createScheduleClass(ScheduleClassDTO dto) {
        if (dto.getLessonId() == null)
            throw new IllegalArgumentException("Lesson ID must be provided.");

        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + dto.getLessonId()));

        lesson.setStatus(StatusClass.CONFIRMED);
        Teacher teacher = lesson.getTeacher();

        if (teacher != null) {
            List<ScheduleClass> conflicts = scheduleClassRepository.findConflictingClasses(
                    teacher.getTeacherId(), dto.getDate(), dto.getStartTime(), dto.getEndTime()
            );
            if (!conflicts.isEmpty())
                throw new ScheduleConflictException("Conflict with teacher schedule on " + dto.getDate());
        }

        ScheduleClass scheduleClass = dto.toScheduleClass();
        scheduleClass.setTeacher(teacher);
        scheduleClass.setLesson(lesson);
        scheduleClass.setClassShift(lesson.getClassShift());

        ScheduleClass saved = scheduleClassRepository.save(scheduleClass);

        scheduleStatisticsService.incrementScheduledClasses();

        lesson.setScheduleClass(saved);
        lessonRepository.save(lesson);

        return ScheduleClassDTO.fromScheduleClass(saved);
    }

    @Transactional
    public ScheduleClassDTO updateScheduleClass(ScheduleClassDTO dto, UUID idClass) {
        ScheduleClass existing = scheduleClassRepository.findById(idClass)
                .orElseThrow(
                        () -> new ScheduleClassRepositoryNotFoundException("Schedule Class not found with id: " + idClass));

        if (existing.getTeacher() != null) {
            List<ScheduleClass> conflicts = scheduleClassRepository.findConflictingClasses(
                            existing.getTeacher().getTeacherId(), dto.getDate(), dto.getStartTime(), dto.getEndTime()
                    ).stream()
                    .filter(sc -> !sc.getIdClassSchedule().equals(existing.getIdClassSchedule())) // evita conflito consigo mesmo
                    .toList();

            if (!conflicts.isEmpty())
                throw new ScheduleConflictException("Conflict with teacher schedule on " + dto.getDate());
        }

        existing.setDate(dto.getDate());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());

        if (dto.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(dto.getLessonId())
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + dto.getLessonId()));

            if (lesson.getAvailableSlots() <= 0)
                throw new NoAvailableSlotsException("Lesson does not have available slots.");

            lesson.setStatus(StatusClass.CONFIRMED);
            lesson.setAvailableSlots(lesson.getAvailableSlots() - 1);
            lessonRepository.save(lesson);

            existing.setLesson(lesson);
        } else {
            existing.setLesson(null);
        }

        ScheduleClass updated = scheduleClassRepository.save(existing);
        return ScheduleClassDTO.fromScheduleClass(updated);
    }

    @Transactional
    public void deleteScheduleClass(UUID idScheduleClass) {
        ScheduleClass scheduleClass = scheduleClassRepository.findById(idScheduleClass)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("ScheduleClass not found"));

        int totalStudents = scheduleClass.getScheduleClassStudents().size();
        for (int i = 0; i < totalStudents; i++) {
            scheduleStatisticsService.decrementActiveStudents();
        }

        if (scheduleClass.getLesson() != null) {
            scheduleClass.getLesson().getStudents().clear();
            lessonRepository.delete(scheduleClass.getLesson());
            scheduleClass.setLesson(null);
        }

        scheduleClassStudentRepository.deleteByScheduleClassId(idScheduleClass);
        scheduleClassTeacherRepository.deleteByScheduleClassId(idScheduleClass);
        scheduleClassRepository.delete(scheduleClass);
        scheduleStatisticsService.decrementScheduledClasses();
    }

    public List<ScheduleRequestRecord> getAvailableClassesForEnrollment() {
        return scheduleClassRepository.findAll().stream()
                .filter(sc -> sc.getLesson() != null && sc.getLesson().getAvailableSlots() > 0)
                .map(ScheduleRequestRecord::fromScheduleClass)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addStudentToScheduleClass(UUID scheduleClassId, UUID studentId) {
        ScheduleClass sc = scheduleClassRepository.findById(scheduleClassId)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("Class not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Lesson lesson = sc.getLesson();
        if (lesson == null)
            throw new LessonNotFoundException("Lesson not found");

        if (lesson.getAvailableSlots() <= 0)
            throw new NoAvailableSlotsException("No available slots");

        ScheduleClassStudent scs = new ScheduleClassStudent();
        scs.setScheduleClass(sc);
        scs.setStudent(student);
        scs.setStartTime(sc.getStartTime());
        scs.setEndTime(sc.getEndTime());

        scheduleClassStudentRepository.save(scs);
        sc.getScheduleClassStudents().add(scs);

        lesson.setAvailableSlots(lesson.getAvailableSlots() - 1);
        lesson.addStudent(student);
        lessonRepository.save(lesson);
        scheduleClassRepository.save(sc);

        Enrollment enrollment = enrollmentRepository.findByStudentAndScheduleClass(student, sc)
                .orElse(new Enrollment(student, sc, StatusClass.CONFIRMED));
        enrollment.setStatus(StatusClass.CONFIRMED);
        enrollmentService.saveEnrollment(enrollment);
    }

    @Transactional
    public void removeStudentFromScheduleClass(UUID scheduleClassId, UUID studentId) {
        ScheduleClass sc = scheduleClassRepository.findById(scheduleClassId)
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("Class not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Lesson lesson = sc.getLesson();
        if (lesson == null)
            throw new LessonNotFoundException("Lesson not found");

        ScheduleClassStudent toRemove = sc.getScheduleClassStudents().stream()
                .filter(scs -> scs.getStudent().equals(student))
                .findFirst()
                .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("Association not found"));

        scheduleClassStudentRepository.delete(toRemove);
        sc.getScheduleClassStudents().remove(toRemove);

        lesson.setAvailableSlots(lesson.getAvailableSlots() + 1);
        lesson.removeStudent(student);
        lessonRepository.save(lesson);

        Enrollment enrollment = enrollmentRepository.findByStudentAndScheduleClass(student, sc)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);

        scheduleClassRepository.save(sc);

        scheduleStatisticsService.decrementActiveStudents();
    }
}
