package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Address;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.ScheduleClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduleClassRepository extends JpaRepository<ScheduleClass, UUID> {

}
