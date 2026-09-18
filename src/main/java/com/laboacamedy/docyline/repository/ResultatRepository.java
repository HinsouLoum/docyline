package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repository pour la gestion des resultats de quiz. */
@Repository
public interface ResultatRepository extends JpaRepository<Resultat,Long> {
    List<Resultat> findByQuizId(Long quizId);
    List<Resultat> findByCandidatId(Long candidatId);
}
