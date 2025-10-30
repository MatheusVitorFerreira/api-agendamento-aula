package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {


    @Query("SELECT s FROM Schedule s " +
            "WHERE s.lesson.teacher.teacherId = :teacherId " +
            "AND s.date = :date " +
            "AND s.startTime < :endTime " +
            "AND s.endTime > :startTime")
    List<Schedule> findOverlappingSchedules(
            @Param("teacherId") UUID teacherId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}