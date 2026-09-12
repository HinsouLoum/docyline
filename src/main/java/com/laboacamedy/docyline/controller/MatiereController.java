package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.MatiereRequest;
import com.laboacamedy.docyline.entities.Matiere;
import com.laboacamedy.docyline.service.MatiereService;
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
 * Controleur REST de gestion des matieres.
 * Consultation publique (GET) ; creation/modification/suppression reservees Admin + Gestionnaire.
 */
@RestController
@RequestMapping("/api/matieres")
@RequiredArgsConstructor
@Tag(
        name = "Matières",
        description = "Gestion des matières (disciplines d'études) disponibles sur la plateforme. " +
                "Les opérations de lecture sont publiques, les modifications sont réservées aux administrateurs. " +
                "Les matières sont associées aux documents et concours."
)
public class MatiereController {

    private final MatiereService matiereService;

    @GetMapping
    @Operation(
            summary = "Lister toutes les matières",
            description = "Récupère la liste complète de toutes les matières disponibles sur la plateforme. " +
                    "Les matières sont les disciplines d'études (Mathématiques, Français, Informatique, etc.). " +
                    "Endpoint public, aucune authentification requise.",
            tags = {"Matières"},
            operationId = "listerToutes"
    )
    public ResponseEntity<List<Matiere>> listerToutes() {
        return ResponseEntity.ok(matiereService.listerToutes());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une matière par son ID",
            description = "Récupère les détails complets d'une matière spécifique en fonction de son identifiant. " +
                    "Endpoint public, aucune authentification requise.",
            tags = {"Matières"},
            operationId = "obtenirParId"
    )
    public ResponseEntity<Matiere> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(matiereService.obtenirParId(id));
    }

    @PostMapping
    @Operation(
            summary = "Créer une nouvelle matière",
            description = "Permet à un administrateur ou gestionnaire de créer une nouvelle matière. " +
                    "Les champs obligatoires sont 'nom' et 'description'. L'ID et la date de création sont générés automatiquement.",
            tags = {"Matières"},
            operationId = "creer",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Matiere> creer(@Valid @RequestBody MatiereRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matiereService.creer(requete));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une matière existante",
            description = "Permet à un administrateur ou gestionnaire de modifier les détails d'une matière existante. " +
                    "L'ID de la matière et la date de création restent inchangés.",
            tags = {"Matières"},
            operationId = "modifier",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Matiere> modifier(@PathVariable Long id, @Valid @RequestBody MatiereRequest requete) {
        return ResponseEntity.ok(matiereService.modifier(id, requete));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une matière",
            description = "Permet à un administrateur ou gestionnaire de supprimer une matière. " +
                    "⚠️ ATTENTION : C'est une suppression physique (hard delete) et non une suppression logique. " +
                    "Assurez-vous qu'aucun document ou concours n'est associé à cette matière avant suppression, " +
                    "car cela pourrait causer des erreurs d'intégrité référentielle. " +
                    "Retourne 204 No Content en cas de succès.",
            tags = {"Matières"},
            operationId = "supprimer",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        matiereService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
