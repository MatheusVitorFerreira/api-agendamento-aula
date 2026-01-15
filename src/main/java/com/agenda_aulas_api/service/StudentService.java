package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.exception.erros.DatabaseNegatedAccessException;
import com.agenda_aulas_api.exception.erros.StudentNotFoundException;
import com.agenda_aulas_api.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Map<String, Object>> findAll() {
        try {
            return studentRepository.findAll()
                    .stream()
                    .map(student -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("studentId", student.getStudentId());
                        map.put("fullName", student.getFullName());
                        map.put("birthDate", student.getBirthDate());
                        map.put("cpf", student.getCpf());
                        return map;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException(
                    "Failed to list students: " + e.getMessage()
            );
        }
    }

    public Student findById(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with id: " + id)
                );
    }

    public Page<Map<String, Object>> findPage(
            Integer page,
            Integer size,
            String orderBy,
            String direction) {

        try {
            PageRequest pageRequest = PageRequest.of(
                    page,
                    size,
                    Sort.Direction.valueOf(direction),
                    orderBy
            );

            return studentRepository.findAll(pageRequest)
                    .map(student -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("studentId", student.getStudentId());
                        map.put("fullName", student.getFullName());
                        map.put("cpf", student.getCpf());
                        return map;
                    });

        } catch (IllegalArgumentException e) {
            throw new DatabaseNegatedAccessException(
                    "Invalid pagination parameters: " + e.getMessage()
            );
        }
    }

    // ATUALIZAR
    @Transactional
    public Student update(UUID id, Student obj) {

        try {
            Student existing = studentRepository.findById(id)
                    .orElseThrow(() ->
                            new StudentNotFoundException("Student not found with id: " + id)
                    );

            existing.setFullName(obj.getFullName());
            existing.setBirthDate(obj.getBirthDate());
            existing.setAge(obj.getAge());
            existing.setEmail(obj.getEmail());
            existing.setCpf(obj.getCpf());
            existing.setTelephone(obj.getTelephone());
            existing.setAddress(obj.getAddress());

            return studentRepository.save(existing);

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException(
                    "Failed to update student: " + e.getMessage()
            );
        }
    }

    // DELETAR
    @Transactional
    public void delete(UUID id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with id: " + id)
                );

        studentRepository.delete(student);
    }
}
