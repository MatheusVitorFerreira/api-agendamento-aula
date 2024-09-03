package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.ScheduleClassStudent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleClassStudentRepository extends JpaRepository<ScheduleClassStudent, UUID> {

    @Query("SELECT scs FROM ScheduleClassStudent scs WHERE scs.scheduleClass = :scheduleClass")
    List<ScheduleClassStudent> findByScheduleClass(@Param("scheduleClass") ScheduleClass scheduleClass);

    @Query("SELECT scs FROM ScheduleClassStudent scs WHERE scs.scheduleClass.id = :scheduleClassId")
    List<ScheduleClassStudent> findByScheduleClassId(@Param("scheduleClassId") UUID scheduleClassId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ScheduleClassStudent scs WHERE scs.scheduleClass.id = :scheduleClassId")
    void deleteByScheduleClassId(@Param("scheduleClassId") UUID scheduleClassId);
}
