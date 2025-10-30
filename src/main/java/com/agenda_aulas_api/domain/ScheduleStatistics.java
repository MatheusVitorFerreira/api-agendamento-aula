package com.agenda_aulas_api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Long totalScheduledClasses = 0L;
    private Long totalScheduledStudentsActive = 0L;
    private Long totalStudents = 0L;
}
