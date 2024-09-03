package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Enrollment;
import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.StatusClass;
import com.agenda_aulas_api.dto.EnrollmentDTO;
import com.agenda_aulas_api.exception.erros.DatabaseNegatedAccessException;
import com.agenda_aulas_api.exception.erros.EnrollmentNotFoundException;
import com.agenda_aulas_api.exception.erros.InvalidUrlException;
import com.agenda_aulas_api.repository.EnrollmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public EnrollmentDTO saveEnrollment(Enrollment enrollment) {
        try {
            Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
            return EnrollmentDTO.fromEnrollment(savedEnrollment);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to save enrollment to the database: " + e.getMessage());
        }
    }

    @Transactional
    public void enrollStudent(Student student, ScheduleClass scheduleClass) {
        try {
            Enrollment enrollment = new Enrollment(student, scheduleClass, StatusClass.CONFIRMED);
            saveEnrollment(enrollment);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to enroll student: " + e.getMessage());
        }
    }

    public List<EnrollmentDTO> getAllEnrollments() {
        try {
            return enrollmentRepository.findAll().stream()
                    .map(EnrollmentDTO::fromEnrollment)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public EnrollmentDTO findById(UUID id) {
        try {
            Enrollment enrollment = enrollmentRepository.findById(id)
                    .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));
            return EnrollmentDTO.fromEnrollment(enrollment);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public Page<EnrollmentDTO> findPageEnrollments(
            Integer page,
            Integer linesPerPage,
            String orderBy,
            String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.fromString(direction), orderBy);
            return enrollmentRepository.findAll(pageRequest).map(EnrollmentDTO::fromEnrollment);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }
}
