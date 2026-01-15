package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "tb_students")
@Getter
@Setter
public class Student extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID studentId;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @ManyToMany(mappedBy = "students")
    private Set<Classroom> classrooms = new HashSet<>();

    @PrePersist
    void onEnroll() {
        this.enrollmentDate = LocalDate.now();
    }
}