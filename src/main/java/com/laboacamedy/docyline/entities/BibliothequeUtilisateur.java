package com.laboacamedy.docyline.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entite BibliothequeUtilisateur : represente la collection de documents
 * achetes/possedes par un utilisateur.
 */
@Entity
@Table(name = "bibliotheques_utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BibliothequeUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    // Documents possedes par l'utilisateur
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "bibliotheque_documents",
            joinColumns = @JoinColumn(name = "bibliotheque_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    @Builder.Default
    private List<Document> documents = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }
}
