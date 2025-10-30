package com.agenda_aulas_api.dto; // Ou seu pacote de DTOs

import com.agenda_aulas_api.domain.Classroom;
import com.agenda_aulas_api.domain.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDTO {

    private UUID idClass;
    private String name;
    private String description;
    private UUID teacherId;
    private LocalDateTime createdAt;

    private Integer studentCount;
    private Integer lessonCount;


    public static ClassroomDTO fromEntity(Classroom classroom) {
        if (classroom == null) {
            throw new IllegalArgumentException("Classroom cannot be null");
        }

        return new ClassroomDTO(
                classroom.getIdClass(),
                classroom.getName(),
                classroom.getDescription(),
                classroom.getTeacher() != null ? classroom.getTeacher().getTeacherId() : null,
                classroom.getCreatedAt(),
                classroom.getStudents() != null ? classroom.getStudents().size() : 0,
                classroom.getLessons() != null ? classroom.getLessons().size() : 0
        );
    }

    public Classroom toEntity() {
        Classroom classroom = new Classroom();
        classroom.setIdClass(this.idClass);
        classroom.setName(this.name);
        classroom.setDescription(this.description);

        if (this.teacherId != null) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(this.teacherId);
            classroom.setTeacher(teacher);
        }
        return classroom;
    }
}