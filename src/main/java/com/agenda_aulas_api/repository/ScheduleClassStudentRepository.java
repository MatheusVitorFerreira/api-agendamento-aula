package com.agenda_aulas_api.repository;


import com.agenda_aulas_api.domain.ScheduleClassStudent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
;
import java.util.UUID;

@Repository
public interface ScheduleClassStudentRepository extends JpaRepository<ScheduleClassStudent, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ScheduleClassStudent scs WHERE scs.scheduleClass.idClassSchedule = :idScheduleClass")
    void deleteByScheduleClassId(UUID idScheduleClass);
}

