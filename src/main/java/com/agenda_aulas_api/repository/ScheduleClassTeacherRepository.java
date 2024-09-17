package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.ScheduleClassTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ScheduleClassTeacherRepository extends JpaRepository<ScheduleClassTeacher, UUID> {

    @Modifying
    @Query("DELETE FROM ScheduleClassTeacher sct WHERE sct.scheduleClass.id = :idScheduleClass")
    void deleteByScheduleClassId(@Param("idScheduleClass") UUID idScheduleClass);
}
