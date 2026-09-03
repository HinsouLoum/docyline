package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReponseRepository extends JpaRepository<Reponse,Long> {
    List<Reponse> findByQuestionId(Long questionId);
}
