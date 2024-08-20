package com.agenda_aulas_api.dto;

import com.agenda_aulas_api.domain.ScheduleClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleClassDTO {

    private UUID idClassSchedule;
    private List<DayOfWeek> weekDays;
    private UUID lessonId;

    public static ScheduleClassDTO fromScheduleClass(ScheduleClass scheduleClass) {
        return new ScheduleClassDTO(
                scheduleClass.getIdClassSchedule(),
                scheduleClass.getWeekDays(),
                scheduleClass.getLesson() != null ?
                        scheduleClass.getLesson().getIdLesson() : null
        );
    }

    public ScheduleClass toScheduleClass() {
        ScheduleClass scheduleClass = new ScheduleClass();
        scheduleClass.setIdClassSchedule(this.idClassSchedule);
        scheduleClass.setWeekDays(this.weekDays);
        return scheduleClass;
    }
}
