package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.StatusClass;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.ClassShift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {

    private UUID idLesson;
    private UUID teacherId;
    private UUID disciplineId;
    private int availableSlots;
    private StatusClass status;
    private String location;
    private List<UUID> students;
    private UUID idClassSchedule;
    private ClassShift classShift;

    public static LessonDTO fromLesson(Lesson lesson) {
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson cannot be null");
        }

        return new LessonDTO(
                lesson.getIdLesson(),
                lesson.getTeacher() != null ? lesson.getTeacher().getTeacherId() : null,
                lesson.getDiscipline() != null ? lesson.getDiscipline().getIdDiscipline() : null,
                lesson.getAvailableSlots(),
                lesson.getStatus(),
                lesson.getLocation(),
                lesson.getStudents() != null
                        ? lesson.getStudents().stream()
                        .map(Student::getStudentId)
                        .collect(Collectors.toList())
                        : List.of(),
                lesson.getScheduleClass() != null ? lesson.getScheduleClass().getIdClassSchedule() : null,
                lesson.getScheduleClass() != null ? lesson.getScheduleClass().getClassShift() : null
        );
    }

    public Lesson toLesson() {
        Lesson lesson = new Lesson();
        lesson.setIdLesson(this.idLesson);
        lesson.setAvailableSlots(this.availableSlots);
        lesson.setStatus(this.status);
        lesson.setLocation(this.location);

        if (this.idClassSchedule != null) {
            ScheduleClass scheduleClass = new ScheduleClass();
            scheduleClass.setIdClassSchedule(this.idClassSchedule);
            scheduleClass.setClassShift(this.classShift);
            lesson.setScheduleClass(scheduleClass);
        }

        return lesson;
    }
}
