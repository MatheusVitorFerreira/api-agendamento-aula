package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Classroom;
import com.agenda_aulas_api.domain.MuralPost;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.Teacher;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record ClassroomRequestRecordDTO(
        UUID idClass,
        @NonNull String name,
        String description,
        @NonNull UUID teacherId,
        Set<UUID> studentIds,
        List<MuralPostRequestDTO> muralPosts,
        LocalDateTime createdAt
) {

    public Classroom toEntity() {
        Classroom classroom = new Classroom();
        classroom.setIdClass(this.idClass);
        classroom.setName(this.name);
        classroom.setDescription(this.description);

        Teacher teacher = new Teacher();
        teacher.setTeacherId(this.teacherId);
        classroom.setTeacher(teacher);

        if (this.studentIds != null) {
            classroom.setStudents(this.studentIds.stream()
                    .map(studentId -> {
                        Student student = new Student();
                        student.setStudentId(studentId);
                        return student;
                    })
                    .collect(Collectors.toSet()));
        }

        if (this.muralPosts != null) {
            List<MuralPost> posts = this.muralPosts.stream()
                    .map(postDTO -> {
                        MuralPost post = postDTO.toEntity();
                        post.setParentId(classroom.getIdClass());
                        post.setParentType("CLASSROOM");
                        return post;
                    })
                    .collect(Collectors.toList());
            classroom.setMuralPosts(posts);
        }

        return classroom;
    }
    public static ClassroomRequestRecordDTO fromEntity(Classroom classroom) {
        if (classroom == null) return null;

        return ClassroomRequestRecordDTO.builder()
                .idClass(classroom.getIdClass())
                .name(classroom.getName())
                .description(classroom.getDescription())
                .createdAt(classroom.getCreatedAt())
                .teacherId(classroom.getTeacher() != null
                        ? classroom.getTeacher().getTeacherId()
                        : null)
                .studentIds(classroom.getStudents() != null
                        ? classroom.getStudents().stream()
                        .map(Student::getStudentId)
                        .collect(Collectors.toSet())
                        : Set.of())
                .muralPosts(classroom.getMuralPosts() != null
                        ? classroom.getMuralPosts().stream()
                        .map(MuralPostRequestDTO::fromEntity)
                        .collect(Collectors.toList())
                        : List.of())
                .build();
    }
}