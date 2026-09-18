package com.laboacamedy.docyline.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entite ConsultationDocument : enregistre chaque consultation/telechargement
 * de document par un utilisateur.
 */
@Entity
@Table(name = "consultations_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    // Type de consultation : VISUALISATION, TELECHARGEMENT
    @Column(nullable = false, length = 50)
    private String typeConsultation;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateConsultation;

    // Nombre de fois que le document a ete consulte cette journee
    @Column(nullable = false)
    @Builder.Default
    private Integer nombreConsultations = 1;

    @PrePersist
    protected void onCreate() {
        this.dateConsultation = LocalDateTime.now();
    }

}
