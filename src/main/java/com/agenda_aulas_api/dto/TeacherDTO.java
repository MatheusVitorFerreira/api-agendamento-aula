package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Teacher;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID idTeacher;
    private String fullName;
    private String job;
    private List<UUID> disciplineIds = new ArrayList<>();
    private LocalDate birthDateTime;
    private int age;
    private String email;
    private String cpf;
    private String telephone;
    private AddressDTO address;

    public Teacher toTeacher() {
        Teacher teacher = new Teacher();
        teacher.setIdTeacher(this.idTeacher);
        teacher.setFullName(this.fullName);
        teacher.setJob(this.job);
        teacher.setBirthDateTime(this.birthDateTime);
        teacher.setAge(this.age);
        teacher.setEmail(this.email);
        teacher.setCpf(this.cpf);
        teacher.setTelephone(this.telephone);

        if (this.address != null) {
            teacher.setAddress(this.address.toAddress());
        }

        return teacher;
    }

    public static TeacherDTO fromTeacher(Teacher teacher) {
        return new TeacherDTO(
                teacher.getIdTeacher(),
                teacher.getFullName(),
                teacher.getJob(),
                teacher.getDisciplines().stream().map(d -> d.getIdDiscipline()).toList(),
                teacher.getBirthDateTime(),
                teacher.getAge(),
                teacher.getEmail(),
                teacher.getCpf(),
                teacher.getTelephone(),
                teacher.getAddress() != null ? AddressDTO.fromAddress(teacher.getAddress()) : null
        );
    }
}
