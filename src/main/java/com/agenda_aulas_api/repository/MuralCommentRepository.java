package com.agenda_aulas_api.repository;


import com.agenda_aulas_api.domain.MuralComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MuralCommentRepository extends JpaRepository<MuralComment, UUID> {

}