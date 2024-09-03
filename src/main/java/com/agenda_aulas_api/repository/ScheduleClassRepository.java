package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleClassRepository extends JpaRepository<ScheduleClass, UUID> {

    @Query("SELECT sc FROM ScheduleClass sc " +
            "WHERE sc.teacher = :teacher " +
            "AND :dayOfWeek MEMBER OF sc.weekDays " +
            "AND (sc.startTime < :endTime AND sc.endTime > :startTime)")
    List<ScheduleClass> findConflictingClasses(
            @Param("teacher") Teacher teacher,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
