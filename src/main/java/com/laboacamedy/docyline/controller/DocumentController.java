package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.DocumentRequest;
import com.laboacamedy.docyline.dto.DocumentResponse;
import com.laboacamedy.docyline.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Controleur REST de gestion des documents numeriques.
 * Catalogue et recherche en lecture publique ; ajout/modification reserves Admin + Gestionnaire.
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(
        name = "Documents",
        description = "Gestion du catalogue de documents numériques (PDF et images). " +
                "Les opérations de lecture/recherche/filtrage sont publiques. " +
                "Les opérations de création/modification/suppression sont réservées aux administrateurs. " +
                "Note : Les documents ne sont jamais supprimés, seulement désactivés (soft delete)."
)
public class DocumentController {

    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

//    GET /api/documents : catalogue complet des documents disponibles
    @GetMapping
    @Operation(
            summary = "Lister le catalogue complet des documents",
            description = "Récupère la liste complète de tous les documents disponibles à la vente sur la plateforme. " +
                    "Seuls les documents actifs (non désactivés) sont retournés. Endpoint public.",
            tags = {"Documents"},
            operationId = "listerCatalogue"
    )
    public ResponseEntity<List<DocumentResponse>> listerCatalogue(){
        return ResponseEntity.ok(documentService.listerCatalogue());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir un document par son ID",
            description = "Récupère les détails complets d'un document spécifique en fonction de son identifiant. " +
                    "Endpoint public.",
            tags = {"Documents"},
            operationId = "obtenirParId"
    )
    public ResponseEntity<DocumentResponse> obtenirParId (@PathVariable Long id){
        return ResponseEntity.ok(documentService.obtenirParId(id));
    }

//    GET /api/documents/rechercher par motCle = ... : recherche par titre/mot-cle
    @GetMapping("/rechercher")
    @Operation(
            summary = "Rechercher des documents par mot-clé",
            description = "Effectue une recherche textuelle dans les documents en fonction d'un mot-clé. " +
                    "La recherche porte sur le titre et la description du document. " +
                    "Endpoint public, aucune authentification requise.",
            tags = {"Documents"},
            operationId = "rechercher"
    )
    public ResponseEntity<List<DocumentResponse>> rechercher(@RequestParam String motCle){
        return ResponseEntity.ok(documentService.rechercher(motCle));
    }

    @GetMapping("/filtrer/concours/{concoursId}")
    @Operation(
            summary = "Filtrer les documents par concours",
            description = "Récupère tous les documents associés à un concours spécifique. " +
                    "Exemple : tous les documents relatifs au BEPC 2024. Endpoint public.",
            tags = {"Documents"},
            operationId = "filtrerParConcours"
    )
    public ResponseEntity<List<DocumentResponse>> filtrerPrConcours(@PathVariable Long concoursId){
        return ResponseEntity.ok(documentService.filtrerParConcours(concoursId));
    }

    @GetMapping("/fitrer/matiere/{matiereId}")
    @Operation(
            summary = "Filtrer les documents par matière",
            description = "Récupère tous les documents associés à une matière spécifique. " +
                    "Exemple : tous les documents de Mathématiques. Endpoint public.",
            tags = {"Documents"},
            operationId = "filtrerParMatiere"
    )
    public ResponseEntity<List<DocumentResponse>> filtrerParMatiere(@PathVariable Long matiereId){
        return ResponseEntity.ok(documentService.filtrerParMatiere(matiereId));
    }

    @GetMapping("/filtrer/categorie/{categorieId}")
    @Operation(
            summary = "Filtrer les documents par catégorie",
            description = "Récupère tous les documents associés à une catégorie spécifique. " +
                    "Exemple : tous les documents d'une catégorie donnée. Endpoint public.",
            tags = {"Documents"},
            operationId = "filtrerParCategorie"
    )
    public ResponseEntity<List<DocumentResponse>> filtrerParCategorie(@PathVariable Long categoireId){
        return ResponseEntity.ok(documentService.filtrerParCategorie(categoireId));
    }

    @GetMapping("/plus-vendus")
    @Operation(
            summary = "Lister les documents les plus vendus",
            description = "Récupère la liste des documents avec le plus grand nombre de ventes. " +
                    "Utile pour afficher les bestsellers et recommandations. " +
                    "La liste est généralement limitée aux 10 ou 20 meilleurs ventes. Endpoint public.",
            tags = {"Documents"},
            operationId = "documentsLesPlusVendus"
    )
    public ResponseEntity<List<DocumentResponse>> documentsLesPlusVendus(){
        return ResponseEntity.ok(documentService.documentsLesPlusVendus());
    }

//    POST /api/document : ajout d'un doucment avec upload du fiechier PDF (multipart/form-data)
//    "donnees" est un champ JSON serialise (DocumentRequest), "fichierPdf" et "image" sont les fichiers
    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "Ajouter un nouveau document avec upload de fichiers",
            description = "Permet à un administrateur ou gestionnaire de créer un nouveau document. " +
                    "Accepte multipart/form-data avec : " +
                    "- 'donnees' (JSON sérialisé) : métadonnées du document " +
                    "- 'fichierPdf' : fichier PDF (obligatoire) " +
                    "- 'image' : image de couverture (optionnel). " +
                    "Les fichiers sont stockés et le document est créé avec disponible=true.",
            tags = {"Documents"},
            operationId = "ajouter",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<DocumentResponse> ajouter(@RequestParam("donnees") String donneesJson,
                                                    @RequestParam("fichierPdf") MultipartFile fichierPdf,
                                                    @RequestParam(value = "image", required = false) MultipartFile image) throws Exception{
        DocumentRequest requete = objectMapper.readValue(donneesJson, DocumentRequest.class);
        DocumentResponse reponse = documentService.ajouter(requete,fichierPdf,image);
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un document existant",
            description = "Permet à un administrateur ou gestionnaire de modifier les métadonnées d'un document existant. " +
                    "L'ID du document et la date de création restent inchangés. " +
                    "Les fichiers PDF et image ne peuvent pas être modifiés par cette opération (ajout seulement).",
            tags = {"Documents"},
            operationId = "modifier",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<DocumentResponse> modifier(@PathVariable Long id, @RequestBody DocumentRequest requete){
        return ResponseEntity.ok(documentService.modifier(id,requete));
    }

//    Desactive un document plutot que de le supprimer (conserve l'historique des ventes )
    @PatchMapping("/{id}/statut")
    @Operation(
            summary = "Changer le statut d'un document (disponible/indisponible)",
            description = "Permet à un administrateur ou gestionnaire d'activer ou désactiver la disponibilité d'un document. " +
                    "IMPORTANT : Cet endpoint ne supprime pas le document mais le marque comme indisponible. " +
                    "C'est une suppression logique (soft delete) qui préserve l'historique des ventes. " +
                    "Un document indisponible n'apparaît plus dans le catalogue mais les ventes antérieures restent tracées.",
            tags = {"Documents"},
            operationId = "changerStatut",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> changerStatut(@PathVariable Long id, @RequestParam boolean disponible){
        documentService.changerStatut(id, disponible);
        return ResponseEntity.noContent().build();
    }
}
