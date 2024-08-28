package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.ClassShift;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.StatusClass;
import com.agenda_aulas_api.domain.Student;

import java.util.List;
import java.util.UUID;

public record LessonRequestRecordDTO(
        UUID teacherId,
        UUID disciplineId,
        int availableSlots,
        ClassShift classShift,
        String status,
        List<UUID> students,
        String location
) {

    public LessonRequestRecordDTO(
            UUID teacherId,
            UUID disciplineId,
            int availableSlots,
            ClassShift classShift,
            StatusClass status,
            List<UUID> students,
            String location) {
        this(
                teacherId,
                disciplineId,
                availableSlots,
                classShift,
                status != null ? status.name() : null,
                students,
                location
        );
    }

    public static LessonRequestRecordDTO fromLesson(Lesson lesson) {
        return new LessonRequestRecordDTO(
                lesson.getTeacher() != null ? lesson.getTeacher().getIdTeacher() : null,
                lesson.getDiscipline() != null ? lesson.getDiscipline().getIdDiscipline() : null,
                lesson.getAvailableSlots(),
                lesson.getClassShift(),
                lesson.getStatus(),
                lesson.getStudents().stream().map(Student::getIdStudent).toList(),
                lesson.getLocation()
        );
    }

    public Lesson toLesson() {
        Lesson lesson = new Lesson();
        lesson.setAvailableSlots(availableSlots);
        lesson.setClassShift(classShift);
        lesson.setStatus(status != null ? StatusClass.valueOf(status) : null);
        lesson.setLocation(location);
        return lesson;
    }
}
