package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idEnrollment", updatable = false, unique = true, nullable = false)
    private UUID idEnrollment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "schedule_class_id")
    private ScheduleClass scheduleClass;

    @Enumerated(EnumType.STRING)
    private StatusClass status;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;

    public Enrollment(Student student, ScheduleClass scheduleClass, StatusClass status) {
        this.student = student;
        this.scheduleClass = scheduleClass;
        this.status = status;
        this.enrollmentDate = LocalDateTime.now();
    }
}
