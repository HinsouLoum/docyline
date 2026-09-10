package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** DTO recu pour la creation/modification d'une categorie. */
@Data
public class CategorieRequest {

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotBlank(message = "L'intitule est obligatoire")
    private String intitule;

    private String description;
}
