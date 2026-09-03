package com.laboacamedy.docyline.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

/** DTO recu pour la creation/modification d'un document (hors fichier PDF, envoye a part en multipart). */
@Data
public class DocumentRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotBlank(message = "Le prix est obligatoire")
    @PositiveOrZero(message = "Le prix doit etre positif")
    private BigDecimal prix;

    private Integer annee;
    private String niveau;

    private Long concoursId;
    private Long matiereId;
    private Long categorieId;
}
