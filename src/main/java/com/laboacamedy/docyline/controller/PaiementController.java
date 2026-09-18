package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.PaiementRequest;
import com.laboacamedy.docyline.dto.PaiementResponse;
import com.laboacamedy.docyline.service.PaiementService;
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
 * Contrôleur REST de gestion des paiements.
 */
@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Gestion des paiements de commandes")
public class PaiementController {

    private final PaiementService paiementService;

    @GetMapping
    @Operation(
            summary = "Lister tous les paiements",
            description = "Récupère la liste complète de tous les paiements",
            tags = {"Paiements"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<PaiementResponse>> listerToutes() {
        return ResponseEntity.ok(paiementService.listerToutes());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir un paiement par son ID",
            description = "Récupère les détails complets d'un paiement spécifique",
            tags = {"Paiements"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PaiementResponse> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.obtenirParId(id));
    }

    @GetMapping("/reference/{reference}")
    @Operation(
            summary = "Obtenir un paiement par sa référence",
            description = "Récupère un paiement via sa référence unique",
            tags = {"Paiements"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PaiementResponse> obtenirParReference(@PathVariable String reference) {
        return ResponseEntity.ok(paiementService.obtenirParReference(reference));
    }

    @GetMapping("/commande/{commandeId}")
    @Operation(
            summary = "Lister les paiements d'une commande",
            description = "Récupère tous les paiements associés à une commande",
            tags = {"Paiements"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<List<PaiementResponse>> listerParCommande(@PathVariable Long commandeId) {
        return ResponseEntity.ok(paiementService.listerParCommande(commandeId));
    }

    @PostMapping
    @Operation(
            summary = "Créer un nouveau paiement",
            description = "Crée un nouveau paiement pour une commande",
            tags = {"Paiements"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PaiementResponse> creer(@Valid @RequestBody PaiementRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paiementService.creer(requete));
    }

    @PutMapping("/{id}/confirmer")
    @Operation(
            summary = "Confirmer un paiement",
            description = "Confirme un paiement en attente",
            tags = {"Paiements"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PaiementResponse> confirmer(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.confirmer(id));
    }

    @PutMapping("/{id}/rejeter")
    @Operation(
            summary = "Rejeter un paiement",
            description = "Rejette un paiement en attente",
            tags = {"Paiements"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PaiementResponse> rejeter(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.rejeter(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un paiement",
            description = "Supprime un paiement de la base de données",
            tags = {"Paiements"},
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        paiementService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
