package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idLesson", updatable = false, unique = true, nullable = false)
    private UUID idLesson;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    private LocalTime startTime;
    private LocalTime endTime;
    private int availableSlots;

    @Enumerated(EnumType.STRING)
    private StatusClass status;

    private String location;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "schedule_class_id")
    private ScheduleClass scheduleClass;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "lesson_students",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(idLesson);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(idLesson, lesson.idLesson);
    }

    public void addStudent(Student student) {
        if (students.size() < availableSlots) {
            students.add(student);
        } else {
            throw new IllegalStateException("No available slots");
        }
    }
}
