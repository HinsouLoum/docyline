package com.laboacamedy.docyline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** DTO pour le dashboard/statistiques globales d'un utilisateur acheteur. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardUtilisateurResponse {

    // Informations utilisateur
    private Long utilisateurId;
    private String nomUtilisateur;
    private String emailUtilisateur;

    // Statistiques d'achat
    private Integer nombreDocumentsAchetes;
    private BigDecimal montantTotalDepense;
    private Integer nombreCommandes;
    private Double montantMoyenParCommande;

    // Statistiques de telechargement
    private Integer nombreTelechargements;
    private Double tauxTelechargement;
    private String derniereTelechargeDocument;

    // Statistiques de performance
    private BigDecimal scoreMoyenQuiz;
    private Integer nombreQuizPasses;
    private Double tauxReussite;

    // Informations bibliotheque
    private Integer documentsEnBibliotheque;
    private String categoriePreferee;
    private String documentMostDownloaded;
}
