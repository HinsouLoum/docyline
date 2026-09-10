package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Panier;
import com.laboacamedy.docyline.entities.enums.StatutPanier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PanierRepository extends JpaRepository<Panier, Long> {
//    Recuperer le panier "en cours" d'un utilisateur (il ne doit en avoir qu'un seul actif a la fois)
    Optional<Panier> findByUtilisateurIdAndStatut(Long utilisateurId, StatutPanier statutPanier);
}
