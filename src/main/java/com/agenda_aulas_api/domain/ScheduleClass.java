package com.agenda_aulas_api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_class_schedule", updatable = false, unique = true, nullable = false)
    private UUID idClassSchedule;

    @OneToMany(mappedBy = "scheduleClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "schedule_class_weekdays", joinColumns = @JoinColumn(name = "schedule_class_id"))
    @Column(name = "week_day")
    private List<DayOfWeek> weekDays = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToOne(mappedBy = "scheduleClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Lesson lesson;

    @OneToMany(mappedBy = "scheduleClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleClassTeacher> scheduleClassTeachers = new ArrayList<>();

    @OneToMany(mappedBy = "scheduleClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleClassStudent> scheduleClassStudents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ClassShift classShift;

    public void setLesson(Lesson lesson) {
        if (this.lesson != null) {
            this.lesson.setScheduleClass(null);
        }
        if (lesson != null) {
            lesson.setScheduleClass(this);
        }
        this.lesson = lesson;
    }

    public List<Student> getStudents() {
        return scheduleClassStudents.stream()
                .map(ScheduleClassStudent::getStudent)
                .toList();
    }

    public void addTeacher(ScheduleClassTeacher teacher) {
        scheduleClassTeachers.add(teacher);
        teacher.setScheduleClass(this);
    }

    public void removeTeacher(ScheduleClassTeacher teacher) {
        scheduleClassTeachers.remove(teacher);
        teacher.setScheduleClass(null);
    }

    public void addStudent(ScheduleClassStudent student) {
        scheduleClassStudents.add(student);
        student.setScheduleClass(this);
    }

    public void removeStudent(ScheduleClassStudent student) {
        scheduleClassStudents.remove(student);
        student.setScheduleClass(null);
    }
}
