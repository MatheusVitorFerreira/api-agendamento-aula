package com.agenda_aulas_api.repository;


import com.agenda_aulas_api.domain.MuralPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MuralPostRepository extends JpaRepository<MuralPost, UUID> {
    List<MuralPost> findByParentIdAndParentType(UUID parentId, String parentType);
}