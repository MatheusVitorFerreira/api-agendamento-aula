package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Address;
import com.agenda_aulas_api.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {
}
