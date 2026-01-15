package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Teacher;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    boolean existsByFullNameAndCpf(String fullName, String cpf);

    boolean existsByCpf(@CPF(message = "CPF inválido") String cpf);
}
