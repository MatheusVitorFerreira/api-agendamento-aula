package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.ClassShift;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.StatusClass;
import com.agenda_aulas_api.domain.Student;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
public record LessonRequestRecordDTO(@NonNull UUID teacherId, @NonNull UUID disciplineId, int availableSlots,
                                     @NonNull ClassShift classShift, @NonNull StatusClass status,
                                     List<UUID> students, @NonNull String location) {

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
                .classShift(lesson.getClassShift())
                .status(lesson.getStatus())
                .students(lesson.getStudents().stream()
                        .map(Student::getStudentId)
                        .toList())
                .location(lesson.getLocation())
                .build();
    }
}
