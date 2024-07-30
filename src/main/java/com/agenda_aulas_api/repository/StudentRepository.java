package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    boolean existsByFullNameAndCpf(String fullName, String cpf);
}
