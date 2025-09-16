package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.ClassShift;
import com.agenda_aulas_api.domain.Discipline;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.StatusClass;
import com.agenda_aulas_api.domain.Teacher;
import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record LessonRequestRecordDTO(
        @NonNull UUID teacherId,
        @NonNull UUID disciplineId,
        @NonNull String nameLesson,
        @NonNull int availableSlots,
        @NonNull ClassShift classShift,
        @NonNull StatusClass status,
        @NonNull String location
) {

    public Lesson toLesson() {
        Lesson lesson = new Lesson();
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);

        Discipline discipline = new Discipline();
        discipline.setIdDiscipline(disciplineId);

        lesson.setTeacher(teacher);
        lesson.setDiscipline(discipline);
        lesson.setNameLesson(nameLesson);
        lesson.setAvailableSlots(availableSlots);
        lesson.setClassShift(classShift);
        lesson.setStatus(status);
        lesson.setLocation(location);

        return lesson;
    }

    public static LessonRequestRecordDTO fromLesson(Lesson lesson) {
        return LessonRequestRecordDTO.builder()
                .teacherId(lesson.getTeacher() != null ? lesson.getTeacher().getTeacherId() : null)
                .disciplineId(lesson.getDiscipline() != null ? lesson.getDiscipline().getIdDiscipline() : null)
                .nameLesson(lesson.getNameLesson())
                .availableSlots(lesson.getAvailableSlots())
                .classShift(
                        lesson.getScheduleClass() != null
                                ? lesson.getScheduleClass().getClassShift()
                                : lesson.getClassShift()
                )
                .status(lesson.getStatus())
                .location(lesson.getLocation())
                .build();
    }
}
