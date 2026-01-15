package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.*;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDTO {

    private UUID id;
    private String username;
    private UserType userType;

    private StudentInfoDTO studentInfo;
    private TeacherInfoDTO teacherInfo;
    private AddressDTO address;

    public static UserResponseDTO fromUser(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setUserType(user.getUserType());

        if (user.getStudent() != null) {
            dto.setStudentInfo(StudentInfoDTO.fromStudent(user.getStudent()));

            if (user.getStudent().getAddress() != null) {
                dto.setAddress(AddressDTO.fromAddress(user.getStudent().getAddress()));
            }
        }

        if (user.getTeacher() != null) {
            dto.setTeacherInfo(TeacherInfoDTO.fromTeacher(user.getTeacher()));

            if (user.getTeacher().getAddress() != null) {
                dto.setAddress(AddressDTO.fromAddress(user.getTeacher().getAddress()));
            }
        }

        return dto;
    }
}
