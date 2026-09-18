package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.PaiementRequest;
import com.laboacamedy.docyline.dto.PaiementResponse;

import java.util.List;

/** Interface du service de gestion des paiements. */
public interface PaiementService {
    PaiementResponse creer(PaiementRequest requete);
    PaiementResponse obtenirParId(Long id);
    PaiementResponse obtenirParReference(String reference);
    List<PaiementResponse> listerParCommande(Long commandeId);
    List<PaiementResponse> listerToutes();
    PaiementResponse confirmer(Long id);
    PaiementResponse rejeter(Long id);
    void supprimer(Long id);
}
