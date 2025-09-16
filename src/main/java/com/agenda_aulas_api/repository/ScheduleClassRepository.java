package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.ScheduleClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleClassRepository extends JpaRepository<ScheduleClass, UUID> {

    @Query("""
        SELECT sc 
        FROM ScheduleClass sc
        WHERE sc.teacher.teacherId = :teacherId
          AND sc.date = :date
          AND (sc.startTime < :endTime AND sc.endTime > :startTime)
    """)
    List<ScheduleClass> findConflictingClasses(
            @Param("teacherId") UUID teacherId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
