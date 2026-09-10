package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Concours;
import com.laboacamedy.docyline.entities.enums.StatutConcours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcoursRepository extends JpaRepository<Concours, Long> {
    List<Concours> findByStatut(StatutConcours statutConcours);
    boolean existsByCode(String code);
}
