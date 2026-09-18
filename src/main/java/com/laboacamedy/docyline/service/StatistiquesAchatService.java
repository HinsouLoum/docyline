package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.DashboardUtilisateurResponse;
import com.laboacamedy.docyline.dto.StatistiquesAchatResponse;

import java.util.List;

/** Interface du service de gestion des statistiques d'achat. */
public interface StatistiquesAchatService {
    StatistiquesAchatResponse creerOuObtenirPourUtilisateur(Long utilisateurId);
    StatistiquesAchatResponse obtenirParId(Long id);
    StatistiquesAchatResponse obtenirParUtilisateur(Long utilisateurId);
    StatistiquesAchatResponse incrementerDocumentsAchetes(Long utilisateurId, java.math.BigDecimal montant);
    StatistiquesAchatResponse incrementerTelechargements(Long utilisateurId);
    StatistiquesAchatResponse incrementerCommandes(Long utilisateurId);
    StatistiquesAchatResponse mettreAJourScoreQuiz(Long utilisateurId, Integer score, Integer scoreMax);
    List<StatistiquesAchatResponse> obtenirTopMeilleurClients(int limit);
    List<StatistiquesAchatResponse> obtenirTopPlusAcheteurs(int limit);
    List<StatistiquesAchatResponse> obtenirTopPlusTelecharges(int limit);
    DashboardUtilisateurResponse obtenirDashboardUtilisateur(Long utilisateurId);
    void supprimer(Long id);
}
