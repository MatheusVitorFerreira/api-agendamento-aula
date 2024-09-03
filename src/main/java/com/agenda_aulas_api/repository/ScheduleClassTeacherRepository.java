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

    @Modifying
    @Query("DELETE FROM ScheduleClassTeacher sct WHERE sct.scheduleClass.id = :idScheduleClass")
    void deleteByScheduleClassId(@Param("idScheduleClass") UUID idScheduleClass);
}
