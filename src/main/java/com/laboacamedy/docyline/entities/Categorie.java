package com.laboacamedy.docyline.entities;

import jakarta.persistence.*;
import lombok.*;

/** Entite Categorie : classement des documents (ex: "Annales", "Cours", "Corriges"...). */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 150)
    private String intitule;

    @Column(length = 1000)
    private String description;
}
