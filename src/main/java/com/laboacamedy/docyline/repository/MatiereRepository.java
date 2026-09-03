package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    boolean existsByCode(String code);
}
