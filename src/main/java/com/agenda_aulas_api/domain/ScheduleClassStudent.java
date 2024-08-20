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
@Table(name = "schedule_class_students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleClassStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_scheduling_id", updatable = false, unique = true, nullable = false)
    private UUID studentSchedulingId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "schedule_class_id", nullable = true)
    private ScheduleClass scheduleClass;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
