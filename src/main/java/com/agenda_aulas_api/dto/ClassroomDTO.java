package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Classroom;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.MuralPost;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.Teacher;
import com.agenda_aulas_api.dto.record.MuralPostRequestDTO;
import com.agenda_aulas_api.dto.record.MuralPostRequestRecordDTO;
import com.agenda_aulas_api.dto.record.MuralPostResponseRecordDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassroomDTO {

    private UUID idClass;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    private UUID teacherId;
    private String teacherName;

    private Set<UUID> studentIds;
    private Integer studentCount;

    private List<LessonDTO> lessons;
    private Integer lessonCount;

    private List<MuralPostResponseRecordDTO> muralPosts;

    public static ClassroomDTO fromLesson(Classroom classroom) {
        if (classroom == null) return null;

        return ClassroomDTO.builder()
                .idClass(classroom.getIdClass())
                .name(classroom.getName())
                .description(classroom.getDescription())
                .createdAt(classroom.getCreatedAt())

                .teacherId(classroom.getTeacher() != null ? classroom.getTeacher().getTeacherId() : null)
                .teacherName(classroom.getTeacher() != null ? classroom.getTeacher().getFullName() : null)

                .studentIds(classroom.getStudents() != null
                        ? classroom.getStudents().stream()
                        .map(Student::getStudentId)
                        .collect(Collectors.toSet())
                        : Set.of())
                .studentCount(classroom.getStudents() != null ? classroom.getStudents().size() : 0)

                .lessons(classroom.getLessons() != null
                        ? classroom.getLessons().stream()
                        .map(LessonDTO::fromLesson)
                        .collect(Collectors.toList())
                        : List.of())
                .lessonCount(classroom.getLessons() != null ? classroom.getLessons().size() : 0)

                .muralPosts(classroom.getMuralPosts() != null
                        ? classroom.getMuralPosts().stream()
                        .map(MuralPostResponseRecordDTO::fromEntity)
                        .collect(Collectors.toList())
                        : List.of())
                .build();
    }
}
