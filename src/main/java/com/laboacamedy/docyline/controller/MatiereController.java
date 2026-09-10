package com.laboacamedy.docyline.controller;

import com.laboacamedy.docyline.dto.MatiereRequest;
import com.laboacamedy.docyline.entities.Matiere;
import com.laboacamedy.docyline.service.MatiereService;
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
public class MatiereController {

    private final MatiereService matiereService;

    @GetMapping
    public ResponseEntity<List<Matiere>> listerToutes() {
        return ResponseEntity.ok(matiereService.listerToutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matiere> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(matiereService.obtenirParId(id));
    }

    @PostMapping
    public ResponseEntity<Matiere> creer(@Valid @RequestBody MatiereRequest requete) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matiereService.creer(requete));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Matiere> modifier(@PathVariable Long id, @Valid @RequestBody MatiereRequest requete) {
        return ResponseEntity.ok(matiereService.modifier(id, requete));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        matiereService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
