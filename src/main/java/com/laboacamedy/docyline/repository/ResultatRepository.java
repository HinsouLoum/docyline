package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultatRepository extends JpaRepository<Resultat,Long> {
    List<Resultat> findByCandidatIdOrderByDateDesc(Long candidatId);
    List<Resultat> findByQuizId(Long quizId);
}
