package com.agenda_aulas_api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_lesson", updatable = false, unique = true, nullable = false)
    private UUID idLesson;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonIgnore
    private Teacher teacher;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    private int availableSlots;

    @Enumerated(EnumType.STRING)
    private StatusClass status;

    private String location;

    @Enumerated(EnumType.STRING)
    private ClassShift classShift;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "schedule_class_id")
    @JsonBackReference
    private ScheduleClass scheduleClass;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "lesson_students",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore
    private List<Student> students = new ArrayList<>();

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
        if (this.students == null) {
            this.students = new ArrayList<>();
        }
        if (!this.students.contains(student)) {
            this.students.add(student);
            student.getLessons().add(this);
        }
    }

    public void removeStudent(Student student) {
        if (this.students != null) {
            this.students.remove(student);
            student.getLessons().remove(this);
        }
    }
}
