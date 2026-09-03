package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.DocumentRequest;
import com.laboacamedy.docyline.dto.DocumentResponse;
import com.laboacamedy.docyline.service.DocumentService;
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
public class DocumentController {

    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

//    GET /api/documents : catalogue complet des documents disponibles
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> listerCatalogue(){
        return ResponseEntity.ok(documentService.listerCatalogue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> obtenirParId (@PathVariable Long id){
        return ResponseEntity.ok(documentService.obtenirParId(id));
    }

//    GET /api/documents/rechercher par motCle = ... : recherche par titre/mot-cle
    @GetMapping("/rechercher")
    public ResponseEntity<List<DocumentResponse>> rechercher(@RequestParam String motCle){
        return ResponseEntity.ok(documentService.rechercher(motCle));
    }

    @GetMapping("/filtrer/concours/{concoursId}")
    public ResponseEntity<List<DocumentResponse>> filtrerPrConcours(@PathVariable Long concoursId){
        return ResponseEntity.ok(documentService.filtrerParConcours(concoursId));
    }

    @GetMapping("/fitrer/matiere/{matiereId}")
    public ResponseEntity<List<DocumentResponse>> filtrerParMatiere(@PathVariable Long matiereId){
        return ResponseEntity.ok(documentService.filtrerParMatiere(matiereId));
    }

    @GetMapping("/filtrer/categorie/{categorieId}")
    public ResponseEntity<List<DocumentResponse>> filtrerParCategorie(@PathVariable Long categoireId){
        return ResponseEntity.ok(documentService.filtrerParCategorie(categoireId));
    }

    @GetMapping("/plus-vendus")
    public ResponseEntity<List<DocumentResponse>> documentsLesPlusVendus(){
        return ResponseEntity.ok(documentService.documentsLesPlusVendus());
    }

//    POST /api/document : ajout d'un doucment avec upload du fiechier PDF (multipart/form-data)
//    "donnees" est un champ JSON serialise (DocumentRequest), "fichierPdf" et "image" sont les fichiers
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponse> ajouter(@RequestParam("donnees") String donneesJson,
                                                    @RequestParam("fichierPdf") MultipartFile fichierPdf,
                                                    @RequestParam(value = "image", required = false) MultipartFile image) throws Exception{
        DocumentRequest requete = objectMapper.readValue(donneesJson, DocumentRequest.class);
        DocumentResponse reponse = documentService.ajouter(requete,fichierPdf,image);
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> modifier(@PathVariable Long id, @RequestBody DocumentRequest requete){
        return ResponseEntity.ok(documentService.modifier(id,requete));
    }

//    Desactive un document plutot que de le supprimer (conserve l'historique des ventes )
    @PatchMapping("/{id}/statut")
    public ResponseEntity<Void> changerStatut(@PathVariable Long id, @RequestParam boolean disponible){
        documentService.changerStatut(id, disponible);
        return ResponseEntity.noContent().build();
    }
}
