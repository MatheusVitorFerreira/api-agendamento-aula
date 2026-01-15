package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfoDTO {

    private String fullName;
    private Integer age;
    private LocalDate birthDate;
    private String email;
    private String cpf;
    private String telephone;

    public Student toStudent() {
        Student student = new Student();
        student.setFullName(this.fullName);
        student.setAge(this.age);
        student.setBirthDate(this.birthDate);
        student.setEmail(this.email);
        student.setCpf(this.cpf);
        student.setTelephone(this.telephone);
        return student;
    }

    public static StudentInfoDTO fromStudent(Student student) {
        if (student == null) return null;

        return new StudentInfoDTO(
                student.getFullName(),
                student.getAge(),
                student.getBirthDate(),
                student.getEmail(),
                student.getCpf(),
                student.getTelephone()
        );
    }
}
