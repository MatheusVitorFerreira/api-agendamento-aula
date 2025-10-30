package com.agenda_aulas_api.repository;



import com.agenda_aulas_api.domain.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, UUID> {


}


