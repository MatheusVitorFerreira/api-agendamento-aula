package com.agenda_aulas_api.service;


import com.agenda_aulas_api.domain.Discipline;
import com.agenda_aulas_api.dto.DisciplineDTO;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.DisciplineRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineService {

    @Autowired
    private DisciplineRepository disciplineRepository;

    public List<DisciplineDTO> findAll() {
        try {
            return disciplineRepository.findAll().stream()
                    .map(DisciplineDTO::fromDiscipline)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public DisciplineDTO findById(UUID id) {
        try {
            Discipline discipline = disciplineRepository.findById(id)
                    .orElseThrow(() -> new AddressNotFoundException("Discipline not found with id: " + id));
            return DisciplineDTO.fromDiscipline(discipline);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }

    public Page<DisciplineDTO> findPageDiscipline(Integer page, Integer linesPerPage, String orderBy, String direction) {
        try {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
            return disciplineRepository.findAll(pageRequest).map(DisciplineDTO::fromDiscipline);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException("Invalid URL or sorting parameter: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to access the database: " + e.getMessage());
        }
    }
    @Transactional
    public DisciplineDTO createDiscipline(DisciplineDTO obj) {
        try {
            Discipline discipline = obj.toDiscipline();
            boolean exists = disciplineRepository.existsByName(
                    discipline.getName());
            if (exists) {
                throw new DuplicateEntityException("Discipline with the same attributes already exists.");
            }
            Discipline saveDiciDiscipline = disciplineRepository.save(discipline);
            return DisciplineDTO.fromDiscipline(saveDiciDiscipline);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to save discipline to the database: " + e.getMessage());
        }
    }

    @Transactional
    public DisciplineDTO updateDiscipline(DisciplineDTO objDto, UUID idDiscipline) {
        try {
            Discipline existingDiscipline = disciplineRepository.findById(idDiscipline)
                    .orElseThrow(() -> new AddressNotFoundException("Discipline not found with id: " + idDiscipline));
            existingDiscipline.setName(objDto.getName());
            Discipline updateDiscipline = disciplineRepository.save(existingDiscipline);
            return DisciplineDTO.fromDiscipline(updateDiscipline);
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Failed to update discipline in the database: " + e.getMessage());
        }
    }

    public void deleteAddress(UUID idAddress) {
        Discipline existingDiscipline= disciplineRepository.findById(idAddress)
                .orElseThrow(() -> new AddressNotFoundException("Discipline not found with id: " + idAddress));
        disciplineRepository.deleteById(idAddress);
    }
}
