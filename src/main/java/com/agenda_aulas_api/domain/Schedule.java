package com.agenda_aulas_api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "schedule")
public class Schedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_schedule", updatable = false, unique = true, nullable = false)
    private UUID idSchedule;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private ClassShift shift;

    @OneToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
}
