package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Address;
import com.agenda_aulas_api.domain.Discipline;
import com.agenda_aulas_api.domain.Teacher;
import com.agenda_aulas_api.dto.TeacherDTO;
import com.agenda_aulas_api.excepetion.erros.*;
import com.agenda_aulas_api.repository.AddressRepository;
import com.agenda_aulas_api.repository.DisciplineRepository;
import com.agenda_aulas_api.repository.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AddressRepository addressRepository;

    public List<Map<String, Object>> findAll() {
        try {
            return teacherRepository.findAll()
                    .stream()
                    .map(teacher -> {
                        Map<String, Object> filteredMap = new HashMap<>();
                        filteredMap.put("cpf", teacher.getCpf());
                        filteredMap.put("fullName", teacher.getFullName());
                        filteredMap.put("birthDateTime", teacher.getBirthDateTime());
                        filteredMap.put("idTeacher", teacher.getIdTeacher());
                        filteredMap.put("addressId", teacher.getAddress() != null ? teacher.getAddress().getId() : null);
                        return filteredMap;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public TeacherDTO findById(UUID idTeacher) {
        try {
            Teacher teacher = teacherRepository.findById(idTeacher).orElseThrow
                    (() -> new TeacherRepositoryNotFoundException("Teacher not found with id: " + idTeacher));
            return TeacherDTO.fromTeacher(teacher);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public Page<TeacherDTO> findPageTeacherDTO(
            Integer page,
            Integer linePerPage,
            String orderBy,
            String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    page,
                    linePerPage,
                    Sort.Direction.valueOf(direction),
                    orderBy
            );

            Page<Teacher> teacherPage = teacherRepository.findAll(pageRequest);
            return teacherPage.map(TeacherDTO::fromTeacher);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public TeacherDTO createTeacher(TeacherDTO dto) {
        try {
            Teacher teacher = dto.toTeacher();

            if (dto.getDisciplineIds() != null && !dto.getDisciplineIds().isEmpty()) {
                List<Discipline> disciplines = disciplineRepository.findAllById(dto.getDisciplineIds());

                if (disciplines.size() != dto.getDisciplineIds().size()) {
                    throw new DisciplineRepositoryNotFoundException("Some disciplines were not found.");
                }
                teacher.getDisciplines().addAll(disciplines);
            }

            teacher = teacherRepository.save(teacher);
            return TeacherDTO.fromTeacher(teacher);
        } catch (DuplicateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    @Transactional
    public TeacherDTO updateTeacher(TeacherDTO obj, UUID idTeacher) {
        try {
            Teacher existingTeacher = teacherRepository.findById(idTeacher).orElseThrow(() -> new TeacherRepositoryNotFoundException("Teacher not found with id: " + idTeacher));
            existingTeacher.setFullName(obj.getFullName());
            existingTeacher.setBirthDateTime(obj.getBirthDateTime());
            existingTeacher.setAge(obj.getAge());
            existingTeacher.setEmail(obj.getEmail());
            existingTeacher.setCpf(obj.getCpf());
            existingTeacher.setTelephone(obj.getTelephone());
            existingTeacher.setLimitCoursesByWeek(obj.getLimitCoursesByWeek());
            existingTeacher.setJob(obj.getJob());

            if (obj.getAddress() != null) {
                existingTeacher.setAddress(obj.getAddress().toAddress());
            }

            existingTeacher = teacherRepository.save(existingTeacher);
            return TeacherDTO.fromTeacher(existingTeacher);

        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to update teacher: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteTeacher(UUID id) {
        Teacher student = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherRepositoryNotFoundException("Teacher not found with id: " + id));
        if (student.getAddress() != null) {
            Address address = student.getAddress();
            addressRepository.delete(address);
        }
        teacherRepository.delete(student);
    }
}