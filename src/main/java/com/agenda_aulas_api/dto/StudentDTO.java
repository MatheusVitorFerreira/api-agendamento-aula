package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private UUID idStudent;
    private String fullName;
    private LocalDate birthDateTime;
    private int age;
    private String email;
    private String cpf;
    private String telephone;
    private LocalDate enrollmentDate;
    private AddressDTO address;

    public Student toStudent() {
        Student student = new Student();
        student.setIdStudent(this.idStudent);
        student.setFullName(this.fullName);
        student.setAge(this.age);
        student.setEmail(this.email);
        student.setTelephone(this.telephone);
        student.setEnrollmentDate(this.enrollmentDate);
        student.setBirthDateTime(this.birthDateTime);
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
                student.getIdStudent(),
                student.getFullName(),
                student.getBirthDateTime(),
                student.getAge(),
                student.getEmail(),
                student.getCpf(),
                student.getTelephone(),
                student.getEnrollmentDate(),
                addressDTO
        );
    }
}
