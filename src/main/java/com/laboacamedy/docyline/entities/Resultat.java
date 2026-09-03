package com.laboacamedy.docyline.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * Entite Resultat : resultat obtenu par un candidat a un quiz.
 * cf. besoin fonctionnel "Passage des quiz et calcul des resultats".
 */
@Entity
@Table(name = "resultats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resultat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Score obtenu (somme des points des bonnes reponses)
    @Column(nullable = false)
    private Integer score;

    // Score maximum possible pour ce quiz (utile pour calculer le pourcentage)
    @Column(nullable = false)
    private Integer scoreMax;

    @Column(nullable = false)
    private Double pourcentage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidat_id", nullable = false)
    private Utilisateur candidat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
