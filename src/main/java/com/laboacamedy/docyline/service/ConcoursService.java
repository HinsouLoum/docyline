package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.ConcoursRequest;
import com.laboacamedy.docyline.entities.Concours;

import java.util.List;

/** Interface du service de gestion des concours. */
public interface ConcoursService {
    Concours creer(ConcoursRequest requete);
    Concours modifier(Long id, ConcoursRequest requete);
    void changerStatutConcour(Long id,boolean actif);
    Concours obtenirParId(Long id);
    List<Concours> listerTous();
    List<Concours> listerActifs();
}
