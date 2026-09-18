package com.laboacamedy.docyline.service.serviceImpl;

import com.laboacamedy.docyline.dto.ConsultationDocumentRequest;
import com.laboacamedy.docyline.dto.ConsultationDocumentResponse;
import com.laboacamedy.docyline.entities.ConsultationDocument;
import com.laboacamedy.docyline.entities.Document;
import com.laboacamedy.docyline.entities.Utilisateur;
import com.laboacamedy.docyline.exception.RessourceNonTrouveeException;
import com.laboacamedy.docyline.repository.ConsultationDocumentRepository;
import com.laboacamedy.docyline.repository.DocumentRepository;
import com.laboacamedy.docyline.repository.UtilisateurRepository;
import com.laboacamedy.docyline.service.ConsultationDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** Implementation du service de gestion des consultations de documents. */
@Service
@RequiredArgsConstructor
@Transactional
public class ConsultationDocumentServiceImpl implements ConsultationDocumentService {

    private final ConsultationDocumentRepository consultationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DocumentRepository documentRepository;

    @Override
    public ConsultationDocumentResponse enregistrerConsultation(ConsultationDocumentRequest requete) {
        Utilisateur utilisateur = utilisateurRepository.findById(requete.getUtilisateurId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + requete.getUtilisateurId()));

        Document document = documentRepository.findById(requete.getDocumentId())
                .orElseThrow(() -> new RessourceNonTrouveeException("Document introuvable avec l'id : " + requete.getDocumentId()));

        ConsultationDocument consultation = ConsultationDocument.builder()
                .utilisateur(utilisateur)
                .document(document)
                .typeConsultation(requete.getTypeConsultation())
                .build();

        ConsultationDocument consultationSauvegardee = consultationRepository.save(consultation);
        return mapperVersResponse(consultationSauvegardee);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultationDocumentResponse obtenirParId(Long id) {
        ConsultationDocument consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Consultation introuvable avec l'id : " + id));
        return mapperVersResponse(consultation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDocumentResponse> listerParUtilisateur(Long utilisateurId) {
        if (!utilisateurRepository.existsById(utilisateurId)) {
            throw new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + utilisateurId);
        }
        return consultationRepository.findByUtilisateurId(utilisateurId).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDocumentResponse> listerParDocument(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new RessourceNonTrouveeException("Document introuvable avec l'id : " + documentId);
        }
        return consultationRepository.findByDocumentId(documentId).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDocumentResponse> listerParType(String typeConsultation) {
        return consultationRepository.findByTypeConsultation(typeConsultation).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDocumentResponse> listerConsultationsUtilisateurDepuisDate(Long utilisateurId, LocalDateTime dateDebut) {
        if (!utilisateurRepository.existsById(utilisateurId)) {
            throw new RessourceNonTrouveeException("Utilisateur introuvable avec l'id : " + utilisateurId);
        }
        return consultationRepository.findByUtilisateurAndDateAapres(utilisateurId, dateDebut).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDocumentResponse> listerConsultationsDocumentParType(Long documentId, String typeConsultation) {
        if (!documentRepository.existsById(documentId)) {
            throw new RessourceNonTrouveeException("Document introuvable avec l'id : " + documentId);
        }
        return consultationRepository.findByDocumentAndType(documentId, typeConsultation).stream()
                .map(this::mapperVersResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer compterTelechargementDocument(Long documentId) {
        return (int) consultationRepository.findByDocumentId(documentId).stream()
                .filter(c -> "TELECHARGEMENT".equals(c.getTypeConsultation()))
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer compterVisitesDocument(Long documentId) {
        return (int) consultationRepository.findByDocumentId(documentId).stream()
                .filter(c -> "VISUALISATION".equals(c.getTypeConsultation()))
                .count();
    }

    @Override
    public void supprimer(Long id) {
        ConsultationDocument consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Consultation introuvable avec l'id : " + id));
        consultationRepository.delete(consultation);
    }

    private ConsultationDocumentResponse mapperVersResponse(ConsultationDocument consultation) {
        return ConsultationDocumentResponse.builder()
                .id(consultation.getId())
                .utilisateurId(consultation.getUtilisateur().getId())
                .nomUtilisateur(consultation.getUtilisateur().getNom() + " " + consultation.getUtilisateur().getPrenom())
                .documentId(consultation.getDocument().getId())
                .titreDocument(consultation.getDocument().getTitre())
                .typeConsultation(consultation.getTypeConsultation())
                .dateConsultation(consultation.getDateConsultation())
                .nombreConsultations(consultation.getNombreConsultations())
                .build();
    }
}
