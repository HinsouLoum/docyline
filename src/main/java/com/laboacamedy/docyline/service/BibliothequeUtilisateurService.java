package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.BibliothequeUtilisateurResponse;

/** Interface du service de gestion des bibliotheques utilisateurs. */
public interface BibliothequeUtilisateurService {
    BibliothequeUtilisateurResponse creerOuObtenirPourUtilisateur(Long utilisateurId);
    BibliothequeUtilisateurResponse obtenirParId(Long id);
    BibliothequeUtilisateurResponse obtenirParUtilisateur(Long utilisateurId);
    BibliothequeUtilisateurResponse ajouterDocument(Long bibliothequeId, Long documentId);
    BibliothequeUtilisateurResponse retirerDocument(Long bibliothequeId, Long documentId);
    Boolean verifierPossessionDocument(Long utilisateurId, Long documentId);
    void supprimer(Long id);
}
