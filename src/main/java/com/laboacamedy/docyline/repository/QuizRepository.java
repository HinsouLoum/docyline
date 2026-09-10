package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Quiz;
import com.laboacamedy.docyline.entities.enums.StatutQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByConcoursId(Long concoursId);
    List<Quiz> findByMatiereId(Long matiereId);
    List<Quiz> findByStatut(StatutQuiz statutQuiz);
}
