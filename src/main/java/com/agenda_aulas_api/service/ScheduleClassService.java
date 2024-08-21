package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.ScheduleClassDTO;
import com.agenda_aulas_api.dto.record.LessonRecordDTO;
import com.agenda_aulas_api.dto.record.ScheduleRecord;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.LessonRepository;
import com.agenda_aulas_api.repository.ScheduleClassRepository;
import com.agenda_aulas_api.repository.TimeTableRepository;
import com.agenda_aulas_api.repository.ScheduleClassTeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
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

    public List<ScheduleClassDTO> findAll() {
        try {
            List<ScheduleClass> scheduleClasses = scheduleClassRepository.findAll();
            return scheduleClasses.stream().map(ScheduleClassDTO::fromScheduleClass).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public Page<ScheduleRecord> findPageScheduleClass(Integer page, Integer linesPerPage, String orderBy, String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
            return scheduleClassRepository.findAll(pageRequest).map(ScheduleRecord::fromScheduleClass);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public ScheduleRecord findById(UUID idScheduleClass) {
        try {
            ScheduleClass scheduleClass = scheduleClassRepository.findById(idScheduleClass)
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("ScheduleClass not found with id: "
                            + idScheduleClass));

            LessonRecordDTO lessonRecordDTO = null;
            if (scheduleClass.getLesson() != null) {
                Lesson lesson = scheduleClass.getLesson();
                lessonRecordDTO = new LessonRecordDTO(
                        lesson.getIdLesson(),
                        lesson.getTeacher() != null ? lesson.getTeacher().getIdTeacher() : null,
                        lesson.getDiscipline() != null ? lesson.getDiscipline().getIdDiscipline() : null,
                        lesson.getStartTime(),
                        lesson.getEndTime(),
                        lesson.getAvailableSlots(),
                        lesson.getStatus(),
                        lesson.getStudents().stream().map(Student::getIdStudent).collect(Collectors.toList()),
                        lesson.getLocation()
                );
            }

            return new ScheduleRecord(scheduleClass.getIdClassSchedule(), scheduleClass.getWeekDays(), lessonRecordDTO);
        } catch (ScheduleClassRepositoryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public ScheduleClassDTO createScheduleClass(ScheduleClassDTO scheduleClassDTO) {
        try {
            ScheduleClass scheduleClass = scheduleClassDTO.toScheduleClass();

            if (scheduleClassDTO.getLessonId() != null) {
                Lesson lesson = lessonRepository.findById(scheduleClassDTO.getLessonId())
                        .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + scheduleClassDTO.getLessonId()));

                // Corrigir a verificação de disponibilidade de vagas
                if (lesson.getAvailableSlots()  > 0) { // Corrigido: Se não há vagas disponíveis
                    throw new NoAvailableSlotsException("Cannot schedule class. No available slots.");
                }

                scheduleClass.setLesson(lesson);
                ScheduleClass savedScheduleClass = scheduleClassRepository.save(scheduleClass);

                lesson.setScheduleClass(savedScheduleClass);
                lessonRepository.save(lesson);

                // Criar ScheduleClassTeacher
                for (DayOfWeek dayOfWeek : savedScheduleClass.getWeekDays()) {
                    ScheduleClassTeacher scheduleClassTeacher = new ScheduleClassTeacher();
                    scheduleClassTeacher.setDayOfWeek(dayOfWeek);
                    scheduleClassTeacher.setScheduleClass(savedScheduleClass);
                    scheduleClassTeacher.setLesson(lesson);
                    scheduleClassTeacher.setStartTime(lesson.getStartTime());
                    scheduleClassTeacher.setEndTime(lesson.getEndTime());
                    scheduleClassTeacher.setTeacher(lesson.getTeacher());

                    if (scheduleClassTeacher.getTeacher() == null) {
                        throw new IllegalStateException("Cannot schedule class. Lesson does not have an assigned teacher.");
                    }

                    scheduleClassTeacherRepository.save(scheduleClassTeacher);
                }
                for (DayOfWeek dayOfWeek : savedScheduleClass.getWeekDays()) {
                    TimeTable timeTable = timeTableRepository.findByLessonIdAndDayOfWeek(lesson.getIdLesson(), dayOfWeek)
                            .orElse(new TimeTable());
                    if (timeTable.getStudentSchedulingId() == null) {
                        timeTable.setLesson(lesson);
                        timeTable.setDayOfWeek(dayOfWeek);
                        timeTable.setScheduleClass(savedScheduleClass);
                    }

                    timeTable.setStartTime(lesson.getStartTime());
                    timeTable.setEndTime(lesson.getEndTime());

                    if (timeTable.getStudents() == null || timeTable.getStudents().isEmpty()) {
                        timeTable.setStudents(new ArrayList<>(lesson.getStudents())); // Corrigido
                    }

                    timeTableRepository.save(timeTable);
                }

                return ScheduleClassDTO.fromScheduleClass(savedScheduleClass);
            } else {
                throw new IllegalArgumentException("Lesson ID must be provided to schedule a class.");
            }
        } catch (IllegalArgumentException | NoAvailableSlotsException e) {
            throw new IllegalStateException("Failed to schedule class: " + e.getMessage());
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
                    .orElseThrow(() -> new ScheduleClassRepositoryNotFoundException("ScheduleClass not found"));

            Lesson lesson = scheduleClass.getLesson();
            if(lesson != null){
                lesson.setScheduleClass(null);
                lessonRepository.save(lesson);
                lessonRepository.delete(lesson);
            }
            scheduleClassRepository.delete(scheduleClass);


        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to delete ScheduleClass: " + e.getMessage());
        }
    }
}
