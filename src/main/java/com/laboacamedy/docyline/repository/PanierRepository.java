package com.laboacamedy.docyline.repository;

import com.laboacamedy.docyline.entities.Panier;
import com.laboacamedy.docyline.entities.enums.StatutPanier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Repository pour la gestion des paniers. */
@Repository
public interface PanierRepository extends JpaRepository<Panier,Long> {

    Optional<Panier> findByUtilisateurIdAndStatut(Long utilisateurId, StatutPanier statut);
    List<Panier> findByUtilisateurId(Long utilisateurId);
    List<Panier> findByStatut(StatutPanier statut);
}
