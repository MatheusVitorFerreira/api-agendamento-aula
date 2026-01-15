package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "classroom")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Classroom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "classroom_id ", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID idClass;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "classroom_students",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();

    @Transient
    private List<MuralPost> muralPosts = new ArrayList<>();

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void addLesson(Lesson lesson) {
        lesson.setClassroom(this);
        this.lessons.add(lesson);
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.getClassrooms().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getClassrooms().remove(this);
    }
}
