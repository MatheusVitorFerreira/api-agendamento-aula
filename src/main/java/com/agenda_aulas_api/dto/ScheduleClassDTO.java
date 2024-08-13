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

    private UUID idClass;
    private List<DayOfWeek> weekDays;
    private String location;
    private List<UUID> lessonIds;

    public static ScheduleClassDTO fromScheduleClass(ScheduleClass scheduleClass) {
        return new ScheduleClassDTO(
                scheduleClass.getIdClass(),
                scheduleClass.getWeekDays(),
                scheduleClass.getLocation(),
                scheduleClass.getLessons() != null ?
                        scheduleClass.getLessons().stream()
                                .map(lesson -> lesson.getIdLesson())
                                .toList()
                        : null
        );
    }

    public ScheduleClass toScheduleClass() {
        ScheduleClass scheduleClass = new ScheduleClass();
        scheduleClass.setIdClass(this.idClass);
        scheduleClass.setWeekDays(this.weekDays);
        scheduleClass.setLocation(this.location);
        return scheduleClass;
    }
}
