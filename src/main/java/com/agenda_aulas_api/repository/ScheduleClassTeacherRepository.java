package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.ScheduleClassTeacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleClassTeacherRepository extends JpaRepository<ScheduleClassTeacher, UUID> {

    @Query("SELECT t FROM ScheduleClassTeacher t WHERE t.lesson.id = :lessonId")
    List<ScheduleClassTeacher> findByLessonId(UUID lessonId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ScheduleClassTeacher sct WHERE sct.scheduleClass.idClassSchedule = :idScheduleClass")
    void deleteByScheduleClassId(UUID idScheduleClass);
}



