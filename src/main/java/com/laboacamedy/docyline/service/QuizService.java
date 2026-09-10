package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.QuizRequest;
import com.laboacamedy.docyline.dto.QuizResponse;
import com.laboacamedy.docyline.entities.enums.StatutQuiz;

import java.util.List;

/** Interface du service de gestion des quiz et de leurs questions/reponses. */
public interface QuizService {
    QuizResponse creer(QuizRequest requete);
    QuizResponse modifier(Long id, QuizRequest requete);
    void changerStatut(Long id, StatutQuiz statut);
    QuizResponse obtenirParId(Long id);
    List<QuizResponse> listerTous();
    List<QuizResponse> listerPublies();
    List<QuizResponse> listerParConcours(Long concoursId);
    List<QuizResponse> listerParMatiere(Long matiereId);
    void supprimer(Long id);
}
