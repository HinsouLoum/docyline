package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findByQuizId(Long quizId);
}
