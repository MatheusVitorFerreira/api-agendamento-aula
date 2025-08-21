package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Teacher;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID teacherId;
    private String fullName;
    private List<UUID> disciplineIds = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDateTime;

    private int age;
    private String email;
    @CPF(message = "CPF inválido")
    private String cpf;
    @NotBlank(message = "Telefone é obrigatório")
    private String telephone;
    private AddressDTO address;

    public Teacher toTeacher() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(this.teacherId);
        teacher.setFullName(this.fullName);
        teacher.setBirthDate(this.birthDateTime);
        teacher.setAge(this.age);
        teacher.setEmail(this.email);
        teacher.setCpf(this.cpf);
        teacher.setTelephone(this.telephone);

        if (this.address != null) {
            teacher.setAddress(this.address.toAddress());
        }

        if (teacher.getDisciplines() == null) {
            teacher.setDisciplines(new ArrayList<>());
        }

        return teacher;
    }

    public static TeacherDTO fromTeacher(Teacher teacher) {
        return new TeacherDTO(
                teacher.getTeacherId(),
                teacher.getFullName(),
                teacher.getDisciplines() != null
                        ? teacher.getDisciplines().stream().map(d -> d.getIdDiscipline()).toList()
                        : new ArrayList<>(),
                teacher.getBirthDate(),
                teacher.getAge(),
                teacher.getEmail(),
                teacher.getCpf(),
                teacher.getTelephone(),
                teacher.getAddress() != null ? AddressDTO.fromAddress(teacher.getAddress()) : null
        );
    }
}
