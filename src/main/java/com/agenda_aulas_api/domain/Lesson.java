package com.agenda_aulas_api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_lesson", updatable = false, unique = true, nullable = false)
    private UUID idLesson;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    @JsonBackReference
    private Classroom classroom;



    @Enumerated(EnumType.STRING)
    private StatusClass status = StatusClass.CONFIRMED;

    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Schedule schedule;

    @Transient
    private List<Material> materials = new ArrayList<>();

    @Transient
    private List<MuralPost> posts = new ArrayList<>();

}
