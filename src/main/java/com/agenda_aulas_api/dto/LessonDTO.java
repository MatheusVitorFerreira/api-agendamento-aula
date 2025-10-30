package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {

    private UUID idLesson;
    private String title;
    private String description;
    private UUID teacherId;
    private UUID classroomId;
    private StatusClass status;
    private UUID scheduleId;
    private ClassShift classShift;

    public static LessonDTO fromLesson(Lesson lesson) {
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson cannot be null");
        }

        return new LessonDTO(
                lesson.getIdLesson(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getTeacher() != null ? lesson.getTeacher().getTeacherId() : null,
                lesson.getClassroom() != null ? lesson.getClassroom().getIdClass() : null,
                lesson.getStatus(),
                lesson.getSchedule() != null ? lesson.getSchedule().getIdSchedule() : null,
                lesson.getSchedule() != null ? lesson.getSchedule().getShift() : null
        );
    }

    public Lesson toLesson() {
        Lesson lesson = new Lesson();
        lesson.setIdLesson(this.idLesson);
        lesson.setTitle(this.title);
        lesson.setDescription(this.description);
        lesson.setStatus(this.status);
        if (this.teacherId != null) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(this.teacherId);
            lesson.setTeacher(teacher);
        }
        if (this.classroomId != null) {
            Classroom classroom = new Classroom();
            classroom.setIdClass(this.classroomId);
            lesson.setClassroom(classroom);
        }
        if (this.scheduleId != null) {
            Schedule schedule = new Schedule();
            schedule.setIdSchedule(this.scheduleId);
            schedule.setShift(this.classShift);
            lesson.setSchedule(schedule);
        }

        return lesson;
    }
}
