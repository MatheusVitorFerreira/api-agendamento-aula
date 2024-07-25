package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ScheduleClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idClass", updatable = false, unique = true, nullable = false)
    private UUID idClass;

    private LocalDateTime date;
    private LocalTime startHour;
    private LocalTime endHour;

    @ManyToOne
    @JoinColumn(name = "id_teacher")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "id_discipline", nullable = false)
    private Discipline discipline;

    private String location;

    @Enumerated(EnumType.STRING)
    private StatusClass status;

    @ManyToMany
    @JoinTable(
            name = "schedule_class_students",
            joinColumns = @JoinColumn(name = "id_class"),
            inverseJoinColumns = @JoinColumn(name = "id_student")
    )
    private List<Student> students;
}
