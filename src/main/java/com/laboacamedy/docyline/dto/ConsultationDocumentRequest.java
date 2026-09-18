package com.laboacamedy.docyline.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** DTO pour la creation d'une consultation de document. */
@Data
public class ConsultationDocumentRequest {

    @NotNull(message = "L'ID utilisateur est obligatoire")
    private Long utilisateurId;

    @NotNull(message = "L'ID document est obligatoire")
    private Long documentId;

    @NotNull(message = "Le type de consultation est obligatoire")
    private String typeConsultation;
}
