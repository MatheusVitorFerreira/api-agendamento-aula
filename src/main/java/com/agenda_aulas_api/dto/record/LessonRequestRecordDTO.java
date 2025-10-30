package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.*;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Builder
public record LessonRequestRecordDTO(
        UUID idLesson,
        @NonNull UUID teacherId,
        @NonNull String title,
        String description,
        @NonNull StatusClass status,
        UUID classroomId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        ClassShift shift,
        List<MaterialRequestDTO> materials,
        List<MuralPostRequestDTO> posts
) {

    public Lesson toLesson() {
        Lesson lesson = new Lesson();
        lesson.setIdLesson(this.idLesson);
        lesson.setTitle(this.title);
        lesson.setDescription(this.description);
        lesson.setStatus(this.status);

        Teacher teacher = new Teacher();
        teacher.setTeacherId(this.teacherId);
        lesson.setTeacher(teacher);

        if (this.classroomId != null) {
            Classroom classroom = new Classroom();
            classroom.setIdClass(this.classroomId);
            lesson.setClassroom(classroom);
        }
        if (this.date != null && this.startTime != null && this.endTime != null && this.shift != null) {
            Schedule schedule = Schedule.builder()
                    .date(this.date)
                    .startTime(this.startTime)
                    .endTime(this.endTime)
                    .shift(this.shift)
                    .lesson(lesson)
                    .build();
            lesson.setSchedule(schedule);
        }

        return lesson;
    }

    public static LessonRequestRecordDTO fromLesson(Lesson lesson) {
        if (lesson == null) return null;

        Schedule schedule = lesson.getSchedule();

        return LessonRequestRecordDTO.builder()
                .idLesson(lesson.getIdLesson())
                .teacherId(lesson.getTeacher() != null ? lesson.getTeacher().getTeacherId() : null)
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .status(lesson.getStatus())
                .classroomId(lesson.getClassroom() != null ? lesson.getClassroom().getIdClass() : null)
                .date(schedule != null ? schedule.getDate() : null)
                .startTime(schedule != null ? schedule.getStartTime() : null)
                .endTime(schedule != null ? schedule.getEndTime() : null)
                .shift(schedule != null ? schedule.getShift() : null)

                .materials(lesson.getMaterials().stream()
                        .map(MaterialRequestDTO::fromEntity)
                        .toList())
                .posts(lesson.getPosts().stream()
                        .map(MuralPostRequestDTO::fromEntity)
                        .toList())
                .build();
    }
}