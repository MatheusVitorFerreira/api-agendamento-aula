package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Student;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID idStudent;
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private int age;
    private String email;
    @CPF(message = "CPF inválido")
    private String cpf;
    @NotBlank(message = "Telefone é obrigatório")
    private String telephone;
    private LocalDate enrollmentDate;
    private AddressDTO address;

    public Student toStudent() {
        Student student = new Student();
        student.setStudentId(this.idStudent);
        student.setFullName(this.fullName);
        student.setAge(this.age);
        student.setEmail(this.email);
        student.setTelephone(this.telephone);
        student.setEnrollmentDate(this.enrollmentDate);
        student.setBirthDate(this.birthDate);
        student.setCpf(this.cpf);
        if (this.address != null) {
            student.setAddress(this.address.toAddress());
        }
        return student;
    }

    public static StudentDTO fromStudent(Student student) {
        AddressDTO addressDTO = null;
        if (student.getAddress() != null) {
            addressDTO = AddressDTO.fromAddress(student.getAddress());
        }
        return new StudentDTO(
                student.getStudentId(),
                student.getFullName(),
                student.getBirthDate(),
                student.getAge(),
                student.getEmail(),
                student.getCpf(),
                student.getTelephone(),
                student.getEnrollmentDate(),
                addressDTO
        );
    }
}
