package com.laboacamedy.docyline.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO de creation/modification d'un quiz complet : titre, duree, concours/matiere associes,
 * et l'ensemble de ses questions avec leurs propositions de reponses (creation en une seule requete).
 */
@Data
public class QuizRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotNull(message = "La duree du quiz (en minutes) est obligatoire")
    private Integer dureeMinutes;

    private Long concoursId;
    private Long matiereId;

    @Valid
    private List<QuestionRequest> questions;
}
