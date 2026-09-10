package com.laboacamedy.docyline.entities;

import com.laboacamedy.docyline.entities.enums.StatutPanier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Entite Panier : panier d'achat courant d'un candidat. */
@Entity
@Table(name = "paniers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutPanier statut = StatutPanier.EN_COURS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LignePanier> lignes = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }
}
