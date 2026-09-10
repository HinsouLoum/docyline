package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** DTO recu pour la creation/modification d'une matiere. */
@Data
public class MatiereRequest {

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotBlank(message = "L'intitule est obligatoire")
    private String intitule;

    private String description;
}
