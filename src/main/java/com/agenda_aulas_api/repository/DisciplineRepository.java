package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, UUID> {
    boolean existsByName(String name);
}
