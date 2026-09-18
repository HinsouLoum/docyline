package com.laboacamedy.docyline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** DTO pour la reponse de statistiques d'achat. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatistiquesAchatResponse {

    private Long id;
    private Long utilisateurId;
    private Integer nombreDocumentsAchetes;
    private BigDecimal montantTotalDepense;
    private Integer nombreTelechargements;
    private Integer nombreCommandes;
    private BigDecimal scoreMoyenQuiz;
    private Integer nombreQuizPasses;
    private LocalDateTime derniereDateTelechargement;
    private LocalDateTime datePremiereCommande;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    // Champs calculés
    private Double montantMoyenParCommande;
    private Double tauxTelechargement;
}
