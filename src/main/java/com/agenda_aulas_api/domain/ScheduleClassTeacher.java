package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "schedule_class_teacher")
public class ScheduleClassTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "teacher_scheduling_id", updatable = false, unique = true, nullable = false)
    private UUID teacherSchedulingId;

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
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;


}
