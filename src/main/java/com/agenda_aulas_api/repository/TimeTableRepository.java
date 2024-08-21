package com.agenda_aulas_api.repository;


import com.agenda_aulas_api.domain.TimeTable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, UUID> {
    @Query("SELECT t FROM TimeTable t WHERE t.lesson.id = :lessonId AND t.dayOfWeek = :dayOfWeek")
    Optional<TimeTable> findByLessonIdAndDayOfWeek(@Param("lessonId") UUID lessonId, @Param("dayOfWeek") DayOfWeek dayOfWeek);
}

