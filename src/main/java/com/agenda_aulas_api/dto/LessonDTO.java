package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.StatusClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {

    private UUID idLesson;
    private UUID teacherId;
    private UUID disciplineId;
    private String startTime;
    private String endTime;
    private int availableSlots;
    private StatusClass status;
    private String location;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static LessonDTO fromLesson(Lesson lesson) {
        return new LessonDTO(
                lesson.getIdLesson(),
                lesson.getTeacher() != null ? lesson.getTeacher().getIdTeacher() : null,
                lesson.getDiscipline() != null ? lesson.getDiscipline().getIdDiscipline() : null,
                lesson.getStartTime() != null ? lesson.getStartTime().format(TIME_FORMATTER) : null,
                lesson.getEndTime() != null ? lesson.getEndTime().format(TIME_FORMATTER) : null,
                lesson.getAvailableSlots(),
                lesson.getStatus(),
                lesson.getLocation()
        );
    }

    public Lesson toLesson() {
        Lesson lesson = new Lesson();
        lesson.setIdLesson(this.idLesson);
        lesson.setStartTime(this.startTime != null ? LocalTime.parse(this.startTime, TIME_FORMATTER) : null);
        lesson.setEndTime(this.endTime != null ? LocalTime.parse(this.endTime, TIME_FORMATTER) : null);
        lesson.setAvailableSlots(this.availableSlots);
        lesson.setStatus(this.status);
        lesson.setLocation(this.location);
        return lesson;
    }
}
