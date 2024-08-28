package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, UUID> {

    @Query("SELECT t FROM TimeTable t WHERE t.lesson.id = :lessonId AND t.dayOfWeek = :dayOfWeek")
    Optional<TimeTable> findByLessonIdAndDayOfWeek(@Param("lessonId") UUID lessonId,
                                                   @Param("dayOfWeek") DayOfWeek dayOfWeek);

    @Query("SELECT t FROM TimeTable t " +
            "WHERE t.lesson = :lesson " +
            "AND t.dayOfWeek = :dayOfWeek " +
            "AND (t.startTime < :endTime AND t.endTime > :startTime)")
    List<TimeTable> findByLessonAndDayOfWeekAndTimeRange(
            @Param("lesson") Lesson lesson,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT t FROM TimeTable t " +
            "JOIN t.students s " +
            "WHERE s = :student " +
            "AND t.dayOfWeek IN :weekDays " +
            "AND (t.startTime < :endTime AND t.endTime > :startTime)")
    List<TimeTable> findConflictingTimeTables(
            @Param("student") Student student,
            @Param("weekDays") List<DayOfWeek> weekDays,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
