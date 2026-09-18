package com.laboacamedy.docyline.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entite StatistiquesAchat : enregistre les statistiques d'achat et telechargement
 * pour chaque utilisateur.
 */
@Entity
@Table(name = "statistiques_achats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatistiquesAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    // Nombre total de documents achetes
    @Column(nullable = false)
    @Builder.Default
    private Integer nombreDocumentsAchetes = 0;

    // Montant total depense
    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal montantTotalDepense = BigDecimal.ZERO;

    // Nombre total de telechargements
    @Column(nullable = false)
    @Builder.Default
    private Integer nombreTelechargements = 0;

    // Nombre total de commandes
    @Column(nullable = false)
    @Builder.Default
    private Integer nombreCommandes = 0;

    // Score moyen obtenu aux quiz (en pourcentage)
    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal scoreMoyenQuiz = BigDecimal.ZERO;

    // Nombre de quiz passes
    @Column(nullable = false)
    @Builder.Default
    private Integer nombreQuizPasses = 0;

    // Derniere date de telechargement
    private LocalDateTime derniereDateTelechargement;

    // Date de premiere commande
    private LocalDateTime datePremiereCommande;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateModification = LocalDateTime.now();
    }

}
