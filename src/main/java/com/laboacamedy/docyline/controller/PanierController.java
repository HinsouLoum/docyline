package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.PanierRequest;
import com.laboacamedy.docyline.dto.PanierResponse;
import com.laboacamedy.docyline.service.PanierService;
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
 * Contrôleur REST de gestion des paniers.
 */
@RestController
@RequestMapping("/api/paniers")
@RequiredArgsConstructor
@Tag(name = "Paniers", description = "Gestion des paniers d'achat")
public class PanierController {

    private final PanierService panierService;

    @GetMapping
    @Operation(
            summary = "Lister tous les paniers",
            description = "Récupère la liste complète de tous les paniers",
            tags = {"Paniers"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<PanierResponse>> listerToutes() {
        return ResponseEntity.ok(panierService.listerToutes());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir un panier par son ID",
            description = "Récupère les détails complets d'un panier spécifique",
            tags = {"Paniers"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PanierResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(panierService.obtenirParId(id));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    @Operation(
            summary = "Lister les paniers d'un utilisateur",
            description = "Récupère tous les paniers associés à un utilisateur",
            tags = {"Paniers"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<PanierResponse>> listerParUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(panierService.listerParUtilisateur(utilisateurId));
    }

    @GetMapping("/utilisateur/{utilisateurId}/actuel")
    @Operation(
            summary = "Obtenir le panier actuel d'un utilisateur",
            description = "Récupère le panier en cours (EN_COURS) d'un utilisateur",
            tags = {"Paniers"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PanierResponse> obtenirPanierActuelUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(panierService.obtenirPanierActuelUtilisateur(utilisateurId));
    }

    @PostMapping
    @Operation(
            summary = "Créer un nouveau panier",
            description = "Crée un nouveau panier pour un utilisateur",
            tags = {"Paniers"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PanierResponse> creer(@Valid @RequestBody PanierRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(panierService.creer(requete));
    }

    @DeleteMapping("/{id}/vider")
    @Operation(
            summary = "Vider un panier",
            description = "Supprime tous les articles du panier sans le supprimer lui-même",
            tags = {"Paniers"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> vider(@PathVariable Long id) {
        panierService.vider(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un panier",
            description = "Supprime complètement un panier",
            tags = {"Paniers"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        panierService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
