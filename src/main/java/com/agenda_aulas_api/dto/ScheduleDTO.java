package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.Schedule;
import com.agenda_aulas_api.domain.ClassShift;
import com.agenda_aulas_api.domain.Lesson;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {

    private UUID lessonId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    private ClassShift shift;

    public static ScheduleDTO fromSchedule(Schedule schedule) {
        return new ScheduleDTO(
                schedule.getLesson() != null ? schedule.getLesson().getIdLesson() : null,
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getShift()
        );
    }

    public Schedule toSchedule(Lesson lesson) {
        Schedule schedule = new Schedule();
        schedule.setLesson(lesson);
        schedule.setDate(this.date);
        schedule.setStartTime(this.startTime);
        schedule.setEndTime(this.endTime);
        schedule.setShift(this.shift);
        return schedule;
    }
}
