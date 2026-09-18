package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.ResultatRequest;
import com.laboacamedy.docyline.dto.ResultatResponse;
import com.laboacamedy.docyline.service.ResultatService;
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
 * Contrôleur REST de gestion des résultats de quiz.
 */
@RestController
@RequestMapping("/api/resultats")
@RequiredArgsConstructor
@Tag(name = "Résultats", description = "Gestion des résultats de quiz des candidats")
public class ResultatController {

    private final ResultatService resultatService;

    @GetMapping
    @Operation(
            summary = "Lister tous les résultats",
            description = "Récupère la liste complète de tous les résultats",
            tags = {"Résultats"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ResultatResponse>> listerToutes() {
        return ResponseEntity.ok(resultatService.listerToutes());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir un résultat par son ID",
            description = "Récupère les détails complets d'un résultat spécifique",
            tags = {"Résultats"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ResultatResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(resultatService.obtenirParId(id));
    }

    @GetMapping("/candidat/{candidatId}")
    @Operation(
            summary = "Lister les résultats d'un candidat",
            description = "Récupère tous les résultats de quiz obtenus par un candidat",
            tags = {"Résultats"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ResultatResponse>> listerParCandidat(@PathVariable Long candidatId) {
        return ResponseEntity.ok(resultatService.listerParCandidat(candidatId));
    }

    @GetMapping("/quiz/{quizId}")
    @Operation(
            summary = "Lister les résultats d'un quiz",
            description = "Récupère tous les résultats obtenus pour un quiz spécifique",
            tags = {"Résultats"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<ResultatResponse>> listerParQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(resultatService.listerParQuiz(quizId));
    }

    @PostMapping
    @Operation(
            summary = "Créer un nouveau résultat",
            description = "Enregistre le résultat d'un candidat à un quiz",
            tags = {"Résultats"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ResultatResponse> creer(@Valid @RequestBody ResultatRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resultatService.creer(requete));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un résultat",
            description = "Supprime un résultat de la base de données",
            tags = {"Résultats"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        resultatService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
