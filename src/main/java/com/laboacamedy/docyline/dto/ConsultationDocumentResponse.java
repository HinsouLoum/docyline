package com.laboacamedy.docyline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** DTO pour la reponse de consultation de document. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationDocumentResponse {

    private Long id;
    private Long utilisateurId;
    private String nomUtilisateur;
    private Long documentId;
    private String titreDocument;
    private String typeConsultation;
    private LocalDateTime dateConsultation;
    private Integer nombreConsultations;
}
