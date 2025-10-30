package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.CreateUserDTO;
import com.agenda_aulas_api.dto.UserDTO;
import com.agenda_aulas_api.exception.erros.DatabaseNegatedAccessException;
import com.agenda_aulas_api.exception.erros.UserCreationException;
import com.agenda_aulas_api.repository.RoleRepository;
import com.agenda_aulas_api.repository.StudentRepository;
import com.agenda_aulas_api.repository.TeacherRepository;
import com.agenda_aulas_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void createUser(String id, CreateUserDTO dto) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new UserCreationException("Invalid UUID format.");
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setUserType(dto.userType());

        List<Role> roles = new ArrayList<>(); // Use List ao invés de Set

        if (dto.userType() == UserType.PROFESSOR) {
            Optional<Teacher> optionalTeacher = teacherRepository.findById(userId);
            if (optionalTeacher.isEmpty()) {
                throw new UserCreationException("Professor not found with ID: " + userId);
            }
            user.setProfessorId(userId);

            Role moderatorRole = roleRepository.findById(Role.Values.MODERATOR.getRoleId())
                    .orElseThrow(() -> new UserCreationException("Role MODERATOR not found"));
            roles.add(moderatorRole);

        } else if (dto.userType() == UserType.ALUNO) {
            Optional<Student> optionalStudent = studentRepository.findById(userId);
            if (optionalStudent.isEmpty()) {
                throw new UserCreationException("Student not found with ID: " + userId);
            }
            user.setAlunoId(userId);

            Role basicRole = roleRepository.findById(Role.Values.BASIC.getRoleId())
                    .orElseThrow(() -> new UserCreationException("Role BASIC not found"));
            roles.add(basicRole);
        } else {
            throw new UserCreationException("Invalid user type");
        }

        user.setRoles(roles);

        userRepository.save(user);
    }

    public List<UserDTO> findAll() {
        try {
            return userRepository.findAll().stream()
                    .map(UserDTO::fromUser)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }
}
