package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.ScheduleClassTeacher;
import com.agenda_aulas_api.domain.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleClassTeacherRepository extends JpaRepository<ScheduleClassTeacher, UUID> {

    @Query("SELECT t FROM ScheduleClassTeacher t WHERE t.lesson.id = :lessonId")
    List<ScheduleClassTeacher> findByLessonId(@Param("lessonId") UUID lessonId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ScheduleClassTeacher sct WHERE sct.scheduleClass.idClassSchedule = :idScheduleClass")
    void deleteByScheduleClassId(@Param("idScheduleClass") UUID idScheduleClass);

    @Query("SELECT sct FROM ScheduleClassTeacher sct " +
            "WHERE sct.teacher = :teacher " +
            "AND :dayOfWeek MEMBER OF sct.daysOfWeek " +
            "AND (sct.startTime < :endTime AND sct.endTime > :startTime)")
    List<ScheduleClassTeacher> findByTeacherAndDayOfWeekAndTimeRange(
            @Param("teacher") Teacher teacher,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
