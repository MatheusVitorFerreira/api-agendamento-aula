package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;


@Entity
@Table(name = "tb_teachers")
@Getter
@Setter
public class Teacher extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID teacherId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;
}
