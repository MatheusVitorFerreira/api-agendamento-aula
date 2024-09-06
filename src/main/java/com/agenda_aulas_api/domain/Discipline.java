package com.agenda_aulas_api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
    @Column(name = "id_discipline", updatable = false, unique = true, nullable = false)
    private UUID idDiscipline;

    @NonNull
    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "disciplines")
    @JsonBackReference
    private List<Teacher> teachers = new ArrayList<>();
}
