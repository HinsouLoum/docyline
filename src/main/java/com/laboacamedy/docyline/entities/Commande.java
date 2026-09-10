package com.laboacamedy.docyline.entities;

import com.laboacamedy.docyline.entities.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entite Commande : creee lorsqu'un candidat valide son panier.
 * cf. besoin fonctionnel "Gestion du panier et des commandes".
 */
@Entity
@Table(name = "commandes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Numero unique attribue a chaque commande (ex: CMD-2026-000123)
    @Column(nullable = false, unique = true, length = 30)
    private String numero;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCommande;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutCommande statut = StatutCommande.EN_ATTENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    // Documents inclus dans la commande (copie des lignes du panier au moment de la validation)
    @ManyToMany
    @JoinTable(
            name = "commande_documents",
            joinColumns = @JoinColumn(name = "commande_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    @Builder.Default
    private List<Document> documents = new ArrayList<>();

    @OneToOne(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private Paiement paiement;

    @PrePersist
    protected void onCreate() {
        this.dateCommande = LocalDateTime.now();
    }

}
