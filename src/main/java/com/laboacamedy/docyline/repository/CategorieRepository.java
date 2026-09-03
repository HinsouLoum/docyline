package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie,Long> {
    boolean existsByCode(String code);
}
