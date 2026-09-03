package com.laboacamedy.docyline.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/** Entite LignePanier : une ligne du panier correspondant a un document ajoute. */
@Entity
@Table(name = "lignes_panier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LignePanier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private Integer quantite = 1;

    // Prix "fige" au moment de l'ajout au panier (evite les incoherences si le prix change ensuite)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panier_id", nullable = false)
    private Panier panier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
}
