package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleClassStudent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_scheduling_id", updatable = false, unique = true, nullable = false)
    private UUID studentSchedulingId;

    @ManyToOne
    @JoinColumn(name = "schedule_class_id", nullable = false)
    private ScheduleClass scheduleClass;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Override
    public int hashCode() {
        return Objects.hash(studentSchedulingId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleClassStudent that = (ScheduleClassStudent) o;
        return Objects.equals(studentSchedulingId, that.studentSchedulingId);
    }
}
