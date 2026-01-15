package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Teacher;
import com.agenda_aulas_api.exception.erros.DatabaseNegatedAccessException;
import com.agenda_aulas_api.exception.erros.TeacherNotFoundException;
import com.agenda_aulas_api.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Map<String, Object>> findAll() {
        try {
            return teacherRepository.findAll()
                    .stream()
                    .map(teacher -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("teacherId", teacher.getTeacherId());
                        map.put("fullName", teacher.getFullName());
                        map.put("cpf", teacher.getCpf());
                        map.put("birthDate", teacher.getBirthDate());
                        return map;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException(
                    "Failed to list teachers: " + e.getMessage()
            );
        }
    }

    // BUSCAR POR ID
    public Teacher findById(UUID id) {
        return teacherRepository.findById(id)
                .orElseThrow(() ->
                        new TeacherNotFoundException("Teacher not found with id: " + id)
                );
    }

    // ATUALIZAR
    @Transactional
    public Teacher update(UUID id, Teacher obj) {

        try {
            Teacher existing = teacherRepository.findById(id)
                    .orElseThrow(() ->
                            new TeacherNotFoundException("Teacher not found with id: " + id)
                    );

            existing.setFullName(obj.getFullName());
            existing.setBirthDate(obj.getBirthDate());
            existing.setAge(obj.getAge());
            existing.setEmail(obj.getEmail());
            existing.setCpf(obj.getCpf());
            existing.setTelephone(obj.getTelephone());
            existing.setAddress(obj.getAddress());

            return teacherRepository.save(existing);

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException(
                    "Failed to update teacher: " + e.getMessage()
            );
        }
    }

    // DELETAR
    @Transactional
    public void delete(UUID id) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new TeacherNotFoundException("Teacher not found with id: " + id)
                );

        teacherRepository.delete(teacher);
    }
}
