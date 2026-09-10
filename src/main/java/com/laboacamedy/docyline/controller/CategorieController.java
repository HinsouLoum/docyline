package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.CategorieRequest;
import com.laboacamedy.docyline.entities.Categorie;
import com.laboacamedy.docyline.service.CategorieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controleur REST de gestion des categories.
 * Consultation publique (GET) ; creation/modification/suppression reservees Admin + Gestionnaire.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    @GetMapping
    public ResponseEntity<List<Categorie>> listerToutes() {
        return ResponseEntity.ok(categorieService.listerToutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorie> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.obtenirParId(id));
    }

    @PostMapping
    public ResponseEntity<Categorie> creer(@Valid @RequestBody CategorieRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categorieService.creer(requete));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categorie> modifier(@PathVariable Long id, @Valid @RequestBody CategorieRequest requete) {
        return ResponseEntity.ok(categorieService.modifier(id, requete));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        categorieService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
