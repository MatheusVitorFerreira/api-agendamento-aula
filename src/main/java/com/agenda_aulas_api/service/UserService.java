package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.CreateUserDTO;
import com.agenda_aulas_api.dto.UserResponseDTO;
import com.agenda_aulas_api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromUser)
                .collect(Collectors.toList());
    }
    @Transactional
    public User create(CreateUserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserType(dto.getUserType());

        if (dto.getUserType() == UserType.ALUNO) {

            if (dto.getStudentInfo() == null) {
                throw new IllegalArgumentException("studentInfo é obrigatório para ALUNO");
            }

            Student student = dto.getStudentInfo().toStudent();
            student.setUser(user);
            user.setStudent(student);

            if (dto.getAddress() != null) {
                Address address = dto.getAddress().toAddress();
                address.setStudent(student);
                student.setAddress(address);
            }
        }

        if (dto.getUserType() == UserType.PROFESSOR) {

            if (dto.getTeacherInfo() == null) {
                throw new IllegalArgumentException("teacherInfo é obrigatório para PROFESSOR");
            }

            Teacher teacher = dto.getTeacherInfo().toTeacher();
            teacher.setUser(user);
            user.setTeacher(teacher);

            if (dto.getAddress() != null) {
                Address address = dto.getAddress().toAddress();
                address.setTeacher(teacher);
                teacher.setAddress(address);
            }
        }

        if (dto.getUserType() == UserType.ADMIN) {
            if (dto.getStudentInfo() != null || dto.getTeacherInfo() != null) {
                throw new IllegalArgumentException("ADMIN não pode ter studentInfo ou teacherInfo");
            }
        }
        return userRepository.save(user);
    }

}
