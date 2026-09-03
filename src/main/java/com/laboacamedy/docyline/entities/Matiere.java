package com.laboacamedy.docyline.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/** Entite Matiere : ex. Culture generale, Droit, Mathematiques, Anglais... */
@Entity
@Table(name = "matieres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 150)
    private String intitule;

    @Column(length = 1000)
    private String description;

    @ManyToMany(mappedBy = "matieres")
    @Builder.Default
    private List<Concours> concoursList = new ArrayList<>();
}
