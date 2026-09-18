package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.ConsultationDocumentRequest;
import com.laboacamedy.docyline.dto.ConsultationDocumentResponse;
import com.laboacamedy.docyline.service.ConsultationDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST de gestion des consultations de documents.
 */
@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@Tag(name = "Consultations", description = "Gestion des consultations et téléchargements de documents")
public class ConsultationDocumentController {

    private final ConsultationDocumentService consultationService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une consultation par son ID",
            description = "Récupère les détails d'une consultation spécifique",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ConsultationDocumentResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.obtenirParId(id));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    @Operation(
            summary = "Lister les consultations d'un utilisateur",
            description = "Récupère toutes les consultations effectuées par un utilisateur",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ConsultationDocumentResponse>> listerParUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(consultationService.listerParUtilisateur(utilisateurId));
    }

    @GetMapping("/document/{documentId}")
    @Operation(
            summary = "Lister les consultations d'un document",
            description = "Récupère toutes les consultations pour un document spécifique",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ConsultationDocumentResponse>> listerParDocument(@PathVariable Long documentId) {
        return ResponseEntity.ok(consultationService.listerParDocument(documentId));
    }

    @GetMapping("/type/{typeConsultation}")
    @Operation(
            summary = "Lister les consultations par type",
            description = "Récupère toutes les consultations d'un type spécifique (VISUALISATION, TELECHARGEMENT, etc.)",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ConsultationDocumentResponse>> listerParType(@PathVariable String typeConsultation) {
        return ResponseEntity.ok(consultationService.listerParType(typeConsultation));
    }

    @GetMapping("/utilisateur/{utilisateurId}/depuis")
    @Operation(
            summary = "Lister les consultations d'un utilisateur depuis une date",
            description = "Récupère les consultations effectuées après une date spécifique",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ConsultationDocumentResponse>> listerDepuisDate(
            @PathVariable Long utilisateurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut) {
        return ResponseEntity.ok(consultationService.listerConsultationsUtilisateurDepuisDate(utilisateurId, dateDebut));
    }

    @GetMapping("/document/{documentId}/type/{typeConsultation}")
    @Operation(
            summary = "Lister les consultations d'un document par type",
            description = "Récupère les consultations d'un type spécifique pour un document",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ConsultationDocumentResponse>> listerParDocumentEtType(
            @PathVariable Long documentId,
            @PathVariable String typeConsultation) {
        return ResponseEntity.ok(consultationService.listerConsultationsDocumentParType(documentId, typeConsultation));
    }

    @PostMapping
    @Operation(
            summary = "Enregistrer une consultation",
            description = "Enregistre une nouvelle consultation/téléchargement de document",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ConsultationDocumentResponse> enregistrer(@Valid @RequestBody ConsultationDocumentRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultationService.enregistrerConsultation(requete));
    }

    @GetMapping("/document/{documentId}/telechargements/nombre")
    @Operation(
            summary = "Compter les téléchargements d'un document",
            description = "Récupère le nombre total de téléchargements pour un document",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Integer> compterTelechargements(@PathVariable Long documentId) {
        return ResponseEntity.ok(consultationService.compterTelechargementDocument(documentId));
    }

    @GetMapping("/document/{documentId}/visites/nombre")
    @Operation(
            summary = "Compter les visites d'un document",
            description = "Récupère le nombre total de visualisations pour un document",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Integer> compterVisites(@PathVariable Long documentId) {
        return ResponseEntity.ok(consultationService.compterVisitesDocument(documentId));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une consultation",
            description = "Supprime une consultation de la base de données",
            tags = {"Consultations"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        consultationService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
