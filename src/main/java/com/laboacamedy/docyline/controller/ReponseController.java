package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.ReponseRequest;
import com.laboacamedy.docyline.dto.ReponseResponse;
import com.laboacamedy.docyline.service.ReponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST de gestion des réponses aux questions de quiz.
 */
@RestController
@RequestMapping("/api/reponses")
@RequiredArgsConstructor
@Tag(name = "Réponses", description = "Gestion des réponses aux questions de quiz")
public class ReponseController {

    private final ReponseService reponseService;

    @GetMapping
    @Operation(
            summary = "Lister toutes les réponses",
            description = "Récupère la liste complète de toutes les réponses",
            tags = {"Réponses"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ReponseResponse>> listerToutes() {
        return ResponseEntity.ok(reponseService.listerToutes());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une réponse par son ID",
            description = "Récupère les détails complets d'une réponse spécifique",
            tags = {"Réponses"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ReponseResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(reponseService.obtenirParId(id));
    }

    @GetMapping("/question/{questionId}")
    @Operation(
            summary = "Lister les réponses d'une question",
            description = "Récupère toutes les réponses proposées pour une question spécifique",
            tags = {"Réponses"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ReponseResponse>> listerParQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(reponseService.listerParQuestion(questionId));
    }

    @PostMapping
    @Operation(
            summary = "Créer une nouvelle réponse",
            description = "Ajoute une nouvelle proposition de réponse à une question",
            tags = {"Réponses"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ReponseResponse> creer(@Valid @RequestBody ReponseRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reponseService.creer(requete));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une réponse existante",
            description = "Modifie les détails d'une réponse existante",
            tags = {"Réponses"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ReponseResponse> modifier(@PathVariable Long id, @Valid @RequestBody ReponseRequest requete) {
        return ResponseEntity.ok(reponseService.modifier(id, requete));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une réponse",
            description = "Supprime une réponse de la base de données",
            tags = {"Réponses"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        reponseService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
