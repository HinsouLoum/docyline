package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.CommandeRequest;
import com.laboacamedy.docyline.dto.CommandeResponse;

import java.util.List;

/** Interface du service de gestion des commandes. */
public interface CommandeService {
    CommandeResponse creer(CommandeRequest requete);
    CommandeResponse obtenirParId(Long id);
    CommandeResponse obtenirParNumero(String numero);
    List<CommandeResponse> listerParUtilisateur(Long utilisateurId);
    List<CommandeResponse> listerToutes();
    CommandeResponse annuler(Long id);
    void supprimer(Long id);
}
