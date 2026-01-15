package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonInfoDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private UserType userType;

    private StudentInfoDTO studentInfo;
    private TeacherInfoDTO teacherInfo;
}
