package com.agenda_aulas_api.dto.record;

import com.agenda_aulas_api.domain.Lesson;
import com.agenda_aulas_api.domain.ScheduleClass;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.dto.LessonDTO;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.UUID;

public record ScheduleRequestRecord(
        UUID idClassSchedule,
        LessonDTO lesson,
        String date,
        String startTime,
        String endTime
) {

    public static ScheduleRequestRecord fromScheduleClass(ScheduleClass scheduleClass) {
        LessonDTO lessonDTO = null;

        if (scheduleClass.getLesson() != null) {
            Lesson lesson = scheduleClass.getLesson();
            lessonDTO = LessonDTO.fromLesson(lesson);
        }

        return new ScheduleRequestRecord(
                scheduleClass.getIdClassSchedule(),
                lessonDTO,
                scheduleClass.getDate() != null ? scheduleClass.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null,
                scheduleClass.getStartTime() != null ? scheduleClass.getStartTime().toString() : null,
                scheduleClass.getEndTime() != null ? scheduleClass.getEndTime().toString() : null
        );
    }
}
