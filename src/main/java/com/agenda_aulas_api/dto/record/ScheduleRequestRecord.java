package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.dto.LessonDTO;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ScheduleRequestRecord(
        UUID idClassSchedule,
        List<DayOfWeek> weekDays,
        LessonDTO lesson,
        String startTime,
        String endTime
) {

    public static ScheduleRequestRecord fromScheduleClass(ScheduleClass scheduleClass) {
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
                    lesson.getStudents() != null ?
                            lesson.getStudents().stream().map(Student::getIdStudent).collect(Collectors.toList()) :
                            null,
                    lesson.getScheduleClass() != null ? lesson.getScheduleClass().getIdClassSchedule() : null
            );
        }

        return new ScheduleRequestRecord(
                scheduleClass.getIdClassSchedule(),
                scheduleClass.getWeekDays(),
                lessonDTO,
                scheduleClass.getStartTime() != null ? scheduleClass.getStartTime().toString() : null,
                scheduleClass.getEndTime() != null ? scheduleClass.getEndTime().toString() : null
        );
    }
}
