package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.QuizResponse;
import com.laboacamedy.docyline.dto.ReponseRequest;
import com.laboacamedy.docyline.dto.ReponseResponse;

import java.util.List;

/** Interface du service de gestion des reponses. */
public interface ReponseService {
    ReponseResponse creer(ReponseRequest requete);
    ReponseResponse modifier(Long id, ReponseRequest requete);
    ReponseResponse obtenirParId(Long id);
    List<ReponseResponse> listerParQuestion(Long questionId);
    List<ReponseResponse> listerToutes();
    void supprimer(Long id);
}
