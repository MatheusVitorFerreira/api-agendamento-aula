package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.ScheduleStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduleStatisticsRepository extends JpaRepository<ScheduleStatistics, UUID> {
}
