package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** DTO pour la creation d'un resultat. */
@Data
public class ResultatRequest {

    @NotNull(message = "Le score est obligatoire")
    private Integer score;

    @NotNull(message = "Le score max est obligatoire")
    private Integer scoreMax;

    @NotNull(message = "L'ID candidat est obligatoire")
    private Long candidatId;

    @NotNull(message = "L'ID quiz est obligatoire")
    private Long quizId;
}
