package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
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

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> weekDays;

    private String location;

    @OneToOne(mappedBy = "scheduleClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Lesson lesson;

    @OneToMany(mappedBy =  "scheduleClass", cascade =CascadeType.ALL,orphanRemoval = true)
    private List<TimeTable> timeTables = new ArrayList<>();

    @OneToMany(mappedBy =  "scheduleClass", cascade =CascadeType.ALL,orphanRemoval = true)
    private List<ScheduleClassTeacher> scheduleClassTeachers = new ArrayList<>();

    public void setLesson(Lesson lesson) {
        if (lesson == null) {
            if (this.lesson != null) {
                this.lesson.setScheduleClass(null);
            }
        } else {
            lesson.setScheduleClass(this);
        }
        this.lesson = lesson;
    }
}
