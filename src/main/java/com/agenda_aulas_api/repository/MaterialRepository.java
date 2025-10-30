package com.agenda_aulas_api.repository;

import com.agenda_aulas_api.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {

    List<Material> findByParentIdAndParentType(UUID parentId, String parentType);
}
