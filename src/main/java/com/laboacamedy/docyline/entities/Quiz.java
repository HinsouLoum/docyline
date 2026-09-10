package com.laboacamedy.docyline.entities;

import com.laboacamedy.docyline.entities.enums.StatutQuiz;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entite Quiz : quiz de preparation associe a un concours et une matiere.
 * cf. besoin fonctionnel "Gestion des quiz et exercices".
 */

@Entity
@Table(name = "quiz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(length = 1000)
    private String description;

    // Duree du quiz en minutes
    @Column(nullable = false)
    private Integer dureeMinutes;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutQuiz statut = StatutQuiz.BROUILLON;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concours_id")
    private Concours concours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id")
    private Matiere matiere;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Question> questions = new ArrayList<>();
}
