package com.agenda_aulas_api.controller;

import com.agenda_aulas_api.domain.ScheduleStatistics;
import com.agenda_aulas_api.service.ScheduleStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class ScheduleStatisticsController {

    private final ScheduleStatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<ScheduleStatistics> getStatistics() {
        return ResponseEntity.ok(statisticsService.getCurrentStats());
    }
}
