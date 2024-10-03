package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findByScheduleClassIsNull();
}


