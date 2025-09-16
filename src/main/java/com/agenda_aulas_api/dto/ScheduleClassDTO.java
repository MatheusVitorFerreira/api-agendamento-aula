package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.ScheduleClass;
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
public class ScheduleClassDTO {

    private UUID lessonId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    public static ScheduleClassDTO fromScheduleClass(ScheduleClass scheduleClass) {
        return new ScheduleClassDTO(
                scheduleClass.getLesson() != null ? scheduleClass.getLesson().getIdLesson() : null,
                scheduleClass.getDate(),
                scheduleClass.getStartTime(),
                scheduleClass.getEndTime()
        );
    }

    public ScheduleClass toScheduleClass() {
        ScheduleClass scheduleClass = new ScheduleClass();
        scheduleClass.setDate(this.date);
        scheduleClass.setStartTime(this.startTime);
        scheduleClass.setEndTime(this.endTime);
        return scheduleClass;
    }
}
