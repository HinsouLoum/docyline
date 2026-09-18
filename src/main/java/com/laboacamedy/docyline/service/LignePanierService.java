package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.LignePanierRequest;
import com.laboacamedy.docyline.dto.LignePanierResponse;

import java.util.List;

/** Interface du service de gestion des lignes de panier. */
public interface LignePanierService {
    LignePanierResponse ajouter(LignePanierRequest requete);
    LignePanierResponse modifier(Long id, LignePanierRequest requete);
    LignePanierResponse obtenirParId(Long id);
    List<LignePanierResponse> listerParPanier(Long panierId);
    void supprimer(Long id);
    void supprimerParPanier(Long panierId);
}
