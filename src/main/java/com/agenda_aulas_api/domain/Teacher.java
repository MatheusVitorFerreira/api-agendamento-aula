package com.agenda_aulas_api.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idTeacher", updatable = false, unique = true, nullable = false)
    private UUID idTeacher;

    private String job;

    @ManyToMany
    @JoinTable(
            name = "teacher_discipline",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    @JsonManagedReference
    private List<Discipline> disciplines = new ArrayList<>();


    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<ScheduleClassTeacher> scheduleClassTeachers = new ArrayList<>();
}
