package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInfoDTO {

    private String fullName;
    private String email;
    private String cpf;
    private LocalDate birthDate;

    public Teacher toTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFullName(this.fullName);
        teacher.setEmail(this.email);
        teacher.setCpf(this.cpf);
        teacher.setBirthDate(this.birthDate);
        return teacher;
    }

    public static TeacherInfoDTO fromTeacher(Teacher teacher) {
        if (teacher == null) return null;

        return new TeacherInfoDTO(
                teacher.getFullName(),
                teacher.getEmail(),
                teacher.getCpf(),
                teacher.getBirthDate()
        );
    }
}


