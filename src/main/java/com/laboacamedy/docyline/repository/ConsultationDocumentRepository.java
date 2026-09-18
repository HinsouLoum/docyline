package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.ConsultationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/** Repository pour la gestion des consultations de documents. */
@Repository
public interface ConsultationDocumentRepository extends JpaRepository<ConsultationDocument, Long> {
    List<ConsultationDocument> findByUtilisateurId(Long utilisateurId);
    List<ConsultationDocument> findByDocumentId(Long documentId);
    List<ConsultationDocument> findByTypeConsultation(String typeConsultation);
    List<ConsultationDocument> findByUtilisateurIdAndDocumentId(Long utilisateurId, Long documentId);

    @Query("SELECT c FROM ConsultationDocument c WHERE c.utilisateur.id = :utilisateurId AND c.dateConsultation >= :dateDebut")
    List<ConsultationDocument> findByUtilisateurAndDateAapres(@Param("utilisateurId") Long utilisateurId, @Param("dateDebut") LocalDateTime dateDebut);

    @Query("SELECT c FROM ConsultationDocument c WHERE c.document.id = :documentId AND c.typeConsultation = :type")
    List<ConsultationDocument> findByDocumentAndType(@Param("documentId") Long documentId, @Param("type") String type);
}
