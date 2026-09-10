package com.laboacamedy.docyline.entities;

import com.laboacamedy.docyline.entities.enums.ModePaiement;
import com.laboacamedy.docyline.entities.enums.StatutPaiement;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entite Paiement : transaction rattachee a une commande.
 * cf. besoin fonctionnel "Gestion des paiements".
 */
@Entity
@Table(name = "paiements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    // Reference unique de la transaction, generee par le systeme ou le prestataire de paiement
    @Column(nullable = false, unique = true, length = 50)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePaiement mode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutPaiement statut = StatutPaiement.EN_ATTENTE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false, unique = true)
    private Commande commande;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
