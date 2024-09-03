package com.agenda_aulas_api.dto;


import com.agenda_aulas_api.domain.ScheduleClass;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleClassDTO {

    private List<DayOfWeek> weekDays;
    private UUID lessonId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;




    public static ScheduleClassDTO fromScheduleClass(ScheduleClass scheduleClass) {
        return new ScheduleClassDTO(
                scheduleClass.getWeekDays(),
                scheduleClass.getLesson() != null ? scheduleClass.getLesson().getIdLesson() : null,
                scheduleClass.getStartTime(),
                scheduleClass.getEndTime()
        );
    }

    public ScheduleClass toScheduleClass() {
        ScheduleClass scheduleClass = new ScheduleClass();
        scheduleClass.setWeekDays(this.weekDays);
        scheduleClass.setStartTime(this.startTime);
        scheduleClass.setEndTime(this.endTime);
        return scheduleClass;
    }

}
