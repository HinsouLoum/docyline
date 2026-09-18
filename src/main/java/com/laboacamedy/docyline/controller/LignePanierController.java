package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.LignePanierRequest;
import com.laboacamedy.docyline.dto.LignePanierResponse;
import com.laboacamedy.docyline.service.LignePanierService;
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
 * Contrôleur REST de gestion des lignes de panier.
 */
@RestController
@RequestMapping("/api/lignes-panier")
@RequiredArgsConstructor
@Tag(name = "Lignes Panier", description = "Gestion des articles dans les paniers")
public class LignePanierController {

    private final LignePanierService lignePanierService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une ligne de panier par son ID",
            description = "Récupère les détails d'une ligne de panier spécifique",
            tags = {"Lignes Panier"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<LignePanierResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(lignePanierService.obtenirParId(id));
    }

    @GetMapping("/panier/{panierId}")
    @Operation(
            summary = "Lister les lignes d'un panier",
            description = "Récupère toutes les lignes (articles) d'un panier spécifique",
            tags = {"Lignes Panier"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<LignePanierResponse>> listerParPanier(@PathVariable Long panierId) {
        return ResponseEntity.ok(lignePanierService.listerParPanier(panierId));
    }

    @PostMapping
    @Operation(
            summary = "Ajouter une ligne au panier",
            description = "Ajoute un document au panier",
            tags = {"Lignes Panier"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<LignePanierResponse> ajouter(@Valid @RequestBody LignePanierRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lignePanierService.ajouter(requete));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une ligne de panier",
            description = "Modifie la quantité ou le prix d'une ligne existante",
            tags = {"Lignes Panier"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<LignePanierResponse> modifier(@PathVariable Long id, @Valid @RequestBody LignePanierRequest requete) {
        return ResponseEntity.ok(lignePanierService.modifier(id, requete));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une ligne du panier",
            description = "Supprime un article du panier",
            tags = {"Lignes Panier"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        lignePanierService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/panier/{panierId}/tous")
    @Operation(
            summary = "Supprimer toutes les lignes d'un panier",
            description = "Supprime tous les articles d'un panier",
            tags = {"Lignes Panier"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimerParPanier(@PathVariable Long panierId) {
        lignePanierService.supprimerParPanier(panierId);
        return ResponseEntity.noContent().build();
    }
}
