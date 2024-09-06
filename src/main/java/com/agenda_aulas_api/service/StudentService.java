package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Address;
import com.agenda_aulas_api.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.dto.StudentDTO;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Map<String, Object>> findAll() {
        try {
            return studentRepository.findAll()
                    .stream()
                    .map(student -> {
                        Map map = objectMapper.convertValue(student, Map.class);
                        Map<String, Object> filteredMap = new HashMap<>();
                        filteredMap.put("studentId", map.get("studentId"));
                        filteredMap.put("fullName", map.get("fullName"));
                        filteredMap.put("birthDate", map.get("birthDate"));
                        filteredMap.put("cpf", map.get("cpf"));
                        filteredMap.put("addressId",
                                map.get("address") != null ? ((Map<String, Object>) map.get("address"))
                                        .get("id") : null);
                        return filteredMap;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public StudentDTO findById(UUID idStudent) {
        try {
            Student student = studentRepository.findById(idStudent)
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + idStudent));
            return StudentDTO.fromStudent(student);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public Page<Map<String, Object>> findPageStudentDTO(
            Integer page,
            Integer linesPerPage,
            String orderBy,
            String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    page,
                    linesPerPage,
                    Sort.Direction.valueOf(direction),
                    orderBy
            );

            Page<Student> studentPage = studentRepository.findAll(pageRequest);

            return studentPage.map(student -> {
                Map map = objectMapper.convertValue(student, Map.class);
                Map<String, Object> filteredMap = new HashMap<>();
                filteredMap.put("idStudent", map.get("idStudent"));
                filteredMap.put("fullName", map.get("fullName"));
                filteredMap.put("birthDateTime", map.get("birthDateTime"));
                filteredMap.put("cpf", map.get("cpf"));
                filteredMap.put("addressId",
                        map.get("address") != null ?
                                ((Map<String, Object>) map.get("address")).get("id") :
                                null
                );
                return filteredMap;
            });

        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO obj) {
        try {
            Student student = obj.toStudent();
            boolean existingStudent = studentRepository.existsByFullNameAndCpf(student.getFullName(), student.getCpf());
            if (existingStudent) {
                throw new DuplicateEntityException(
                        "Student already exists with full name: "
                                + student.getFullName()
                                + " and CPF: " + student.getCpf());
            }
            student = studentRepository.save(student);
            return StudentDTO.fromStudent(student);
        } catch (DuplicateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to create student: " + e.getMessage());
        }
    }

    @Transactional
    public StudentDTO updateStudent(StudentDTO obj, UUID idStudent) {
        try {
            Student existingStudent = studentRepository.findById(idStudent)
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + idStudent));

            existingStudent.setFullName(obj.getFullName());
            existingStudent.setBirthDate(obj.getBirthDate());
            existingStudent.setAge(obj.getAge());
            existingStudent.setEmail(obj.getEmail());
            existingStudent.setCpf(obj.getCpf());
            existingStudent.setTelephone(obj.getTelephone());
            existingStudent.setEnrollmentDate(obj.getEnrollmentDate());

            if (obj.getAddress() != null) {
                existingStudent.setAddress(obj.getAddress().toAddress());
            }

            existingStudent = studentRepository.save(existingStudent);
            return StudentDTO.fromStudent(existingStudent);

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to update student: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteStudent(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        if (student.getAddress() != null) {
            Address address = student.getAddress();
            addressRepository.delete(address);
        }
        studentRepository.delete(student);
    }
}
