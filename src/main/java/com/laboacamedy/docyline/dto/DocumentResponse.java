package com.laboacamedy.docyline.dto;


import com.laboacamedy.docyline.enums.StatutDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** DTO renvoye au frontend pour l'affichage d'un document dans le catalogue. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private Long id;
    private String titre;
    private String description;
    private BigDecimal prix;
    private String cheminImage;
    private Integer annee;
    private String niveau;
    private StatutDocument statutDocument;
    private Integer nombreVentes;
    private String nomConcours;
    private String nomMatiere;
    private String nomCategorie;
}
