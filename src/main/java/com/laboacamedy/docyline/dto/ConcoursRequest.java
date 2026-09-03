package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/** DTO recu pour la creation/modification d'un concours. */
@Data
public class ConcoursRequest {

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;
    private String organisme;
    private Integer annee;
    private String niveau;
    private String conditionsMolites;

//     Identifiants des matieres a asscier au concours
    private List<Long> matiereIds;
}
