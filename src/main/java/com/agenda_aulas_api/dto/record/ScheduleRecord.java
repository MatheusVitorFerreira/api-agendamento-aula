package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.Student;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ScheduleRecord(
        UUID idClass,
        List<java.time.DayOfWeek> weekDays,
        LessonRecordDTO lesson
) {
    public static ScheduleRecord fromScheduleClass(ScheduleClass scheduleClass) {
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
                    lesson.getStudents().stream()
                            .map(Student::getIdStudent)
                            .collect(Collectors.toList()),
                    lesson.getLocation()
            );
        }

        return new ScheduleRecord(
                scheduleClass.getIdClassSchedule(),
                scheduleClass.getWeekDays(),
                lessonRecordDTO
        );
    }
}
