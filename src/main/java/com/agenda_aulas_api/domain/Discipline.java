package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discipline implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idDiscipline", updatable = false, unique = true, nullable = false)
    private UUID idDiscipline;

    private String name;

    @ManyToMany(mappedBy = "disciplines")
    private List<Teacher> teachers = new ArrayList<>();
}
