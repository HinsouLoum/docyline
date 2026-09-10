package com.laboacamedy.docyline.entities;

import com.laboacamedy.docyline.entities.enums.StatutConcours;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entite Concours : represente un concours ou examen propose par Labo Academy
 * (ex: ENAM, Douanes, Police, etc.), pour lequel des documents et quiz sont associes.
 */
@Entity
@Table(name = "concours")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Concours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(length = 2000)
    private String description;

    @Column(length = 150)
    private String organisme;

    private Integer annee;

    private String niveau;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutConcours statut = StatutConcours.ACTIF;

    // Conditions et modalites du concours (texte libre)
    @Column(length = 3000)
    private String conditionsModalites;

    // Un concours regroupe plusieurs matieres (relation many-to-many)
    @ManyToMany
    @JoinTable(
            name = "concours_matieres",
            joinColumns = @JoinColumn(name = "concours_id"),
            inverseJoinColumns = @JoinColumn(name = "matiere_id")
    )
    @Builder.Default
    private List<Matiere> matieres = new ArrayList<>();

    // Un concours possede plusieurs documents
    @OneToMany(mappedBy = "concours")
    @Builder.Default
    private List<Document> documents = new ArrayList<>();

    // Un concours possede plusieurs quiz de preparation
    @OneToMany(mappedBy = "concours")
    @Builder.Default
    private List<Quiz> quizzes = new ArrayList<>();

}
