package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Classroom;
import com.agenda_aulas_api.domain.Teacher;
import com.agenda_aulas_api.dto.LessonDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record ClassroomResponseRecordDTO(
        UUID idClass,
        String name,
        String description,
        UUID teacherId,
        List<LessonDTO> lessons,
        LocalDateTime createdAt
) {

    public Classroom toClassRoom() {
        Classroom classroom = new Classroom();
        classroom.setIdClass(this.idClass);
        classroom.setName(this.name);
        classroom.setDescription(this.description);
        classroom.setCreatedAt(this.createdAt);

        if (this.teacherId != null) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(this.teacherId);
            classroom.setTeacher(teacher);
        }

        if (this.lessons != null) {
            classroom.setLessons(
                    this.lessons.stream()
                            .map(LessonDTO::toLesson)
                            .collect(Collectors.toList())
            );
        }

        return classroom;
    }

    public static ClassroomResponseRecordDTO fromClassRoom(Classroom classroom) {
        if (classroom == null) return null;

        return ClassroomResponseRecordDTO.builder()
                .idClass(classroom.getIdClass())
                .name(classroom.getName())
                .description(classroom.getDescription())
                .teacherId(classroom.getTeacher() != null
                        ? classroom.getTeacher().getTeacherId()
                        : null)
                .lessons(classroom.getLessons() != null
                        ? classroom.getLessons().stream()
                        .map(LessonDTO::fromLesson)
                        .collect(Collectors.toList())
                        : List.of())
                .createdAt(classroom.getCreatedAt())
                .build();
    }
}
