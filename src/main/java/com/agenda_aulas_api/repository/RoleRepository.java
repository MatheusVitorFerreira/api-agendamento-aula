package com.agenda_aulas_api.repository;



import com.agenda_aulas_api.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
