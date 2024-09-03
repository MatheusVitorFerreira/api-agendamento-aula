package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Enrollment;
import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.StatusClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDTO {

    private UUID idEnrollment;
    private Student student;
    private ScheduleClass scheduleClass;
    private StatusClass status;
    private LocalDateTime enrollmentDate;

    public Enrollment toEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(this.getIdEnrollment());
        enrollment.setStudent(this.getStudent());
        enrollment.setScheduleClass(this.getScheduleClass());
        enrollment.setStatus(this.getStatus());
        enrollment.setEnrollmentDate(this.getEnrollmentDate());
        return enrollment;
    }
    public static EnrollmentDTO fromEnrollment(Enrollment enrollment) {
        return new EnrollmentDTO(
                enrollment.getIdEnrollment(),
                enrollment.getStudent(),
                enrollment.getScheduleClass(),
                enrollment.getStatus(),
                enrollment.getEnrollmentDate()
        );
    }
}
