package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Enrollment;
import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.scheduleClass = :scheduleClass")
    Optional<Enrollment> findByStudentAndScheduleClass(@Param("student") Student student,
                                                       @Param("scheduleClass") ScheduleClass scheduleClass);

    @Query("SELECT e FROM Enrollment e WHERE e.scheduleClass.id = :scheduleClassId")
    List<Enrollment> findByScheduleClassId(@Param("scheduleClassId") UUID scheduleClassId);
}
