package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {

    private String username;
    private String password;
    private UserType userType;

    private StudentInfoDTO studentInfo;
    private TeacherInfoDTO teacherInfo;
    private AddressDTO address;

    public User toUser() {

        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setUserType(this.userType);

        if (userType == UserType.ALUNO && studentInfo != null) {
            Student student = studentInfo.toStudent();
            student.setUser(user);
            user.setStudent(student);

            if (address != null) {
                Address addr = address.toAddress();
                addr.setStudent(student);
                student.setAddress(addr);
            }
        }
        if (userType == UserType.PROFESSOR && teacherInfo != null) {
            Teacher teacher = teacherInfo.toTeacher();
            teacher.setUser(user);
            user.setTeacher(teacher);

            if (address != null) {
                Address addr = address.toAddress();
                addr.setTeacher(teacher);
                teacher.setAddress(addr);
            }
        }

        return user;
    }
}
