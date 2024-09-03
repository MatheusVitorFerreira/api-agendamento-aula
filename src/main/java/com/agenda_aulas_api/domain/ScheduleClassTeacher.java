package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ScheduleClassTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "teacher_scheduling_id", updatable = false, unique = true, nullable = false)
    private UUID teacherSchedulingId;

    @Column(name = "days_of_week", nullable = false)
    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> daysOfWeek = new ArrayList<>();

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_class_id", nullable = true)
    private ScheduleClass scheduleClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleClassTeacher that = (ScheduleClassTeacher) o;
        return Objects.equals(teacherSchedulingId, that.teacherSchedulingId) &&
                Objects.equals(daysOfWeek, that.daysOfWeek) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(lesson, that.lesson) &&
                Objects.equals(scheduleClass, that.scheduleClass) &&
                Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherSchedulingId, daysOfWeek, startTime, endTime, lesson, scheduleClass, teacher);
    }
}
