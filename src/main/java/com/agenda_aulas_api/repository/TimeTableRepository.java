package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, UUID> {
}
