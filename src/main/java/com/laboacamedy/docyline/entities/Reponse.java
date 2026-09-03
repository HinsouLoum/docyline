package com.laboacamedy.docyline.entities;


import jakarta.persistence.*;
import lombok.*;

/** Entite Reponse : proposition de reponse a une question (une ou plusieurs peuvent etre correctes). */
@Entity
@Table(name = "reponses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String contenu;

    // Indique si cette proposition est la (ou une des) bonne(s) reponse(s)
    @Column(nullable = false)
    @Builder.Default
    private Boolean estCorrecte = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
