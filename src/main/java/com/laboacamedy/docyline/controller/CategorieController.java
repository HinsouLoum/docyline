package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.CategorieRequest;
import com.laboacamedy.docyline.entities.Categorie;
import com.laboacamedy.docyline.service.CategorieService;
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
 * Contrôleur REST de gestion des catégories de documents.

 * Permissions :
 * - GET (lister/obtenir) : Public (accessible à tous)
 * - POST/PUT/DELETE : Réservé aux administrateurs et gestionnaires (ADMIN, GESTIONNAIRE)

 * Les catégories permettent de classer les documents par domaine d'études.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Catégories", description = "Gestion des catégories de documents (Mathématiques, Français, Informatique, etc.). " +
                "Les opérations de lecture sont publiques, les modifications sont réservées aux administrateurs.")
public class CategorieController {

    private final CategorieService categorieService;


    /**
     * Lister toutes les catégories disponibles.
     *
     * @return une liste de toutes les catégories
     */

    @GetMapping
    @Operation(
            summary = "Lister toutes les catégories",
            description = "Récupère la liste complète de toutes les catégories de documents disponibles. " +
                    "Endpoint public, aucune authentification requise.",
            tags = {"Catégories"},
            operationId = "listerToutes"
    )
    public ResponseEntity<List<Categorie>> listerToutes() {
        return ResponseEntity.ok(categorieService.listerToutes());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une catégorie par son ID",
            description = "Récupère les détails complets d'une catégorie spécifique en fonction de son identifiant. " +
                    "Endpoint public, aucune authentification requise.",
            tags = {"Catégories"},
            operationId = "obtenirParId"
    )
    public ResponseEntity<Categorie> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.obtenirParId(id));
    }

    @PostMapping
    @Operation(
            summary = "Créer une nouvelle catégorie",
            description = "Permet à un administrateur ou gestionnaire de créer une nouvelle catégorie de documents. " +
                    "Les champs obligatoires sont 'nom' et 'description'. L'ID et la date de création sont générés automatiquement.",
            tags = {"Catégories"},
            operationId = "creer",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Categorie> creer(@Valid @RequestBody CategorieRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categorieService.creer(requete));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une catégorie existante",
            description = "Permet à un administrateur ou gestionnaire de modifier les détails d'une catégorie existante. " +
                    "L'ID de la catégorie et la date de création restent inchangés.",
            tags = {"Catégories"},
            operationId = "modifier",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<Categorie> modifier(@PathVariable Long id, @Valid @RequestBody CategorieRequest requete) {
        return ResponseEntity.ok(categorieService.modifier(id, requete));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une catégorie",
            description = "Permet à un administrateur ou gestionnaire de supprimer une catégorie. " +
                    "ATTENTION : La suppression d'une catégorie peut affecter les documents associés selon la politique en place. " +
                    "Retourne 204 No Content en cas de succès.",
            tags = {"Catégories"},
            operationId = "supprimer",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        categorieService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
