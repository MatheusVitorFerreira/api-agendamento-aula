package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Person  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idStudent", updatable = false, unique = true, nullable = false)
    private UUID idStudent;

    private LocalDate enrollmentDate;

    @Transient
    private String progress;

    @ManyToMany(mappedBy = "students")
    private List<ScheduleClass> enrolledClasses = new ArrayList<>();

    public void enrollInClass(ScheduleClass scheduleClass) {
        if (!enrolledClasses.contains(scheduleClass)) {
            enrolledClasses.add(scheduleClass);
            scheduleClass.getStudents().add(this);
        }
    }

    public void cancelEnrollment(ScheduleClass scheduleClass) {
        if (enrolledClasses.contains(scheduleClass)) {
            enrolledClasses.remove(scheduleClass);
            scheduleClass.getStudents().remove(this);
        } else {
            throw new IllegalArgumentException("Student is not enrolled in the specified class");
        }
    }
}