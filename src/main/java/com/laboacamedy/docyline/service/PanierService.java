package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.PanierRequest;
import com.laboacamedy.docyline.dto.PanierResponse;

import java.util.List;

/** Interface du service de gestion des paniers. */
public interface PanierService {
    PanierResponse creer(PanierRequest requete);
    PanierResponse obtenirParId(Long id);
    PanierResponse obtenirPanierActuelUtilisateur(Long utilisateurId);
    List<PanierResponse> listerParUtilisateur(Long utilisateurId);
    List<PanierResponse> listerToutes();
    void vider(Long id);
    void supprimer(Long id);
}
