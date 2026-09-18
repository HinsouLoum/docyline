package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.ResultatRequest;
import com.laboacamedy.docyline.dto.ResultatResponse;

import java.util.List;

/** Interface du service de gestion des resultats. */
public interface ResultatService {
    ResultatResponse creer(ResultatRequest requete);
    ResultatResponse obtenirParId(Long id);
    List<ResultatResponse> listerParCandidat(Long candidatId);
    List<ResultatResponse> listerParQuiz(Long quizId);
    List<ResultatResponse> listerToutes();
    void supprimer(Long id);
}
