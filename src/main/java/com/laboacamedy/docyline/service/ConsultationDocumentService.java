package com.laboacamedy.docyline.service;

import com.laboacamedy.docyline.dto.ConsultationDocumentRequest;
import com.laboacamedy.docyline.dto.ConsultationDocumentResponse;

import java.time.LocalDateTime;
import java.util.List;

/** Interface du service de gestion des consultations de documents. */
public interface ConsultationDocumentService {
    ConsultationDocumentResponse enregistrerConsultation(ConsultationDocumentRequest requete);
    ConsultationDocumentResponse obtenirParId(Long id);
    List<ConsultationDocumentResponse> listerParUtilisateur(Long utilisateurId);
    List<ConsultationDocumentResponse> listerParDocument(Long documentId);
    List<ConsultationDocumentResponse> listerParType(String typeConsultation);
    List<ConsultationDocumentResponse> listerConsultationsUtilisateurDepuisDate(Long utilisateurId, LocalDateTime dateDebut);
    List<ConsultationDocumentResponse> listerConsultationsDocumentParType(Long documentId, String typeConsultation);
    Integer compterTelechargementDocument(Long documentId);
    Integer compterVisitesDocument(Long documentId);
    void supprimer(Long id);
}
