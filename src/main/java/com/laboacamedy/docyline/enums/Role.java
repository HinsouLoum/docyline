package com.laboacamedy.docyline.enums;

/**
 * Roles disponibles dans le systeme (cf. section "Acteurs et utilisateurs").
 * Utilise pour la gestion des droits d'acces (matrice des droits d'acces).
 */
public enum Role {
    ADMINISTRATEUR,      // Administrateur Labo Academy : gestion complete de la plateforme
    GESTIONNAIRE,        // Gestionnaire de contenu : gere concours, matieres, documents, quiz
    CANDIDAT              // Candidat / Client : consulte, achete, telecharge, passe les quiz
}
