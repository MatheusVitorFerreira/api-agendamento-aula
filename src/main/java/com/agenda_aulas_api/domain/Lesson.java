package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idLesson", updatable = false, unique = true, nullable = false)
    private UUID idLesson;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "schedule_class_id")
    private ScheduleClass scheduleClass;

    private DayOfWeek weekDay;
    private LocalTime startTime;
    private LocalTime endTime;

    public boolean possibleApply(RescheduleExpiationData expi) {
        return true;
    }
}
