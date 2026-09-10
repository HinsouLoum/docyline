package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.MatiereRequest;
import com.laboacamedy.docyline.entities.Matiere;

import java.util.List;

/** Interface du service de gestion des matieres. */
public interface MatiereService {
    Matiere creer(MatiereRequest requete);
    Matiere modifier(Long id, MatiereRequest requete);
    Matiere obtenirParId(Long id);
    List<Matiere> listerToutes();
    void supprimer(Long id);
}
