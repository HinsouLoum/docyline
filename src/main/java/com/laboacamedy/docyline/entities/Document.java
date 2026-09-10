package com.laboacamedy.docyline.entities;

import com.laboacamedy.docyline.entities.enums.StatutDocument;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entite Document : represente un document numerique vendu par Labo Academy
 * (sujet de concours, corrige, cours, exercice, fiche de revision, etc.).
 * cf. besoin fonctionnel "Gestion des documents".
 */
@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(length = 2000)
    private String description;

    // Prix du document en FCFA
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    // Chemin du fichier PDF stocke sur le serveur (acces protege - non public)
    @Column(nullable = false)
    private String cheminFichierPdf;

    // Chemin de l'image de presentation (miniature du document dans le catalogue)
    private String cheminImage;

    private Integer annee;

    private String niveau;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutDocument statut = StatutDocument.DISPONIBLE;

    // Compteur du nombre de ventes (mis a jour a chaque commande payee)
    @Builder.Default
    private Integer nombreVentes = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateAjout;

    // Un document est rattache a un seul concours
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concours_id")
    private Concours concours;

    // Un document est rattache a une seule matiere
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id")
    private Matiere matiere;

    // Un document est rattache a une seule categorie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @PrePersist
    protected void onCreate() {
        this.dateAjout = LocalDateTime.now();
    }
}
