package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.ScheduleClassStudent;
import com.agenda_aulas_api.domain.ScheduleStatistics;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleStatisticsRepository extends JpaRepository<ScheduleStatistics, UUID> {
}
