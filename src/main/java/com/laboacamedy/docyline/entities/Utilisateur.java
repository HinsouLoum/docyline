package com.laboacamedy.docyline.entities;

import com.laboacamedy.docyline.enums.Role;
import com.laboacamedy.docyline.enums.StatutCompte;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Entite Utilisateur : represente tout compte de la plateforme
 * (administrateur, gestionnaire de contenu ou candidat).
 * cf. besoin fonctionnel "Gestion des utilisateurs".
 */
@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telephone;

    // Mot de passe stocke chiffre (BCrypt) - jamais en clair (besoin non fonctionnel : securite)
    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutCompte statut = StatutCompte.ACTIF;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateDerniereConnexion;

    // Un utilisateur (candidat) peut avoir plusieurs commandes
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Commande> commandes = new ArrayList<>();

    // Un utilisateur (candidat) peut avoir plusieurs resultats de quiz
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Resultat> resultats = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }
}
