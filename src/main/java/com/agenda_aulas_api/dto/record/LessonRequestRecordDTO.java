package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.ClassShift;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.StatusClass;
import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record LessonRequestRecordDTO(
        @NonNull UUID teacherId,
        @NonNull UUID disciplineId,
        int availableSlots,
        @NonNull ClassShift classShift,
        @NonNull StatusClass status,
        @NonNull String location
) {

    public Lesson toLesson() {
        Lesson lesson = new Lesson();
        lesson.setAvailableSlots(availableSlots);
        lesson.setClassShift(classShift);
        lesson.setStatus(status);
        lesson.setLocation(location);
        return lesson;
    }

    public static LessonRequestRecordDTO fromLesson(Lesson lesson) {
        return LessonRequestRecordDTO.builder()
                .teacherId(lesson.getTeacher().getTeacherId())
                .disciplineId(lesson.getDiscipline().getIdDiscipline())
                .availableSlots(lesson.getAvailableSlots())
                .classShift(lesson.getScheduleClass() != null
                        ? lesson.getScheduleClass().getClassShift()
                        : lesson.getClassShift())
                .status(lesson.getStatus())
                .location(lesson.getLocation())
                .build();
    }
}
