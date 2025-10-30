package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Schedule;
import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.ClassShift;
import com.agenda_aulas_api.dto.LessonDTO;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ScheduleRequestRecord(
        UUID scheduleId,
        LessonDTO lesson,
        String date,
        String startTime,
        String endTime,
        String shift
) {

    public static ScheduleRequestRecord fromSchedule(Schedule schedule) {
        LessonDTO lessonDTO = schedule.getLesson() != null
                ? LessonDTO.fromLesson(schedule.getLesson())
                : null;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return new ScheduleRequestRecord(
                schedule.getIdSchedule(),
                lessonDTO,
                schedule.getDate() != null ? schedule.getDate().format(dateFormatter) : null,
                schedule.getStartTime() != null ? schedule.getStartTime().format(timeFormatter) : null,
                schedule.getEndTime() != null ? schedule.getEndTime().format(timeFormatter) : null,
                schedule.getShift() != null ? schedule.getShift().name() : null
        );
    }

    public Schedule toSchedule(Lesson lesson) {
        Schedule schedule = new Schedule();
        schedule.setIdSchedule(this.scheduleId);
        schedule.setLesson(lesson);
        schedule.setDate(this.date != null ? LocalDate.parse(this.date, DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null);
        schedule.setStartTime(this.startTime != null ? LocalTime.parse(this.startTime) : null);
        schedule.setEndTime(this.endTime != null ? LocalTime.parse(this.endTime) : null);
        schedule.setShift(this.shift != null ? ClassShift.valueOf(this.shift) : null);
        return schedule;
    }
}
