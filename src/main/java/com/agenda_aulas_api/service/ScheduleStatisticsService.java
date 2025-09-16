package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;

import com.agenda_aulas_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ScheduleStatisticsService {

    private final ScheduleStatisticsRepository repository;

    private ScheduleStatistics getStats() {
        return repository.findAll().stream()
                .findFirst()
                .orElseGet(() -> repository.save(new ScheduleStatistics()));
    }

    @Transactional
    public void incrementScheduledClasses() {
        ScheduleStatistics stats = getStats();
        stats.setTotalScheduledClasses(stats.getTotalScheduledClasses() + 1);
        repository.save(stats);
    }

    @Transactional
    public void decrementScheduledClasses() {
        ScheduleStatistics stats = getStats();
        stats.setTotalScheduledClasses(Math.max(0, stats.getTotalScheduledClasses() - 1));
        repository.save(stats);
    }

    @Transactional
    public void incrementActiveStudents() {
        ScheduleStatistics stats = getStats();
        stats.setTotalScheduledStudentsActive(stats.getTotalScheduledStudentsActive() + 1);
        repository.save(stats);
    }

    @Transactional
    public void decrementActiveStudents() {
        ScheduleStatistics stats = getStats();
        stats.setTotalScheduledStudentsActive(Math.max(0, stats.getTotalScheduledStudentsActive() - 1));
        repository.save(stats);
    }

    @Transactional
    public void incrementTotalStudents() {
        ScheduleStatistics stats = getStats();
        stats.setTotalStudents(stats.getTotalStudents() + 1);
        repository.save(stats);
    }

    @Transactional
    public void decrementTotalStudents() {
        ScheduleStatistics stats = getStats();
        stats.setTotalStudents(Math.max(0, stats.getTotalStudents() - 1));
        repository.save(stats);
    }

    public ScheduleStatistics getCurrentStats() {
        return getStats();
    }
}