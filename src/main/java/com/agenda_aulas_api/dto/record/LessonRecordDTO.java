package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.StatusClass;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record LessonRecordDTO(
        UUID idLesson,
        UUID teacherId,
        UUID disciplineId,
        String startTime,
        String endTime,
        int availableSlots,
        String status,
        List<UUID> students,
        String location
) {

    public LessonRecordDTO(UUID idLesson,
                           UUID teacherId,
                           UUID disciplineId,
                           LocalTime startTime,
                           LocalTime endTime,
                           int availableSlots,
                           StatusClass status,
                           List<UUID> students,
                           String location) {
        this(
                idLesson,
                teacherId,
                disciplineId,
                startTime != null ? startTime.toString() : null,
                endTime != null ? endTime.toString() : null,
                availableSlots,
                status != null ? status.name() : null,
                students,
                location
        );
    }

    public static LessonRecordDTO fromLesson(Lesson lesson) {
        return new LessonRecordDTO(
                lesson.getIdLesson(),
                lesson.getTeacher() != null ? lesson.getTeacher().getIdTeacher() : null,
                lesson.getDiscipline() != null ? lesson.getDiscipline().getIdDiscipline() : null,
                lesson.getStartTime(),
                lesson.getEndTime(),
                lesson.getAvailableSlots(),
                lesson.getStatus(),
                lesson.getStudents().stream().map(student -> student.getIdStudent()).toList(),
                lesson.getLocation()
        );
    }
}
